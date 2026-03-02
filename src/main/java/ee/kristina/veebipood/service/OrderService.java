package ee.kristina.veebipood.service;

import ee.kristina.veebipood.dto.OrderProductDto;
import ee.kristina.veebipood.entity.*;
import ee.kristina.veebipood.model.*;
import ee.kristina.veebipood.repository.OrderRepository;
import ee.kristina.veebipood.repository.PersonRepository;
import ee.kristina.veebipood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${everypay.username}")
    private String username;

    @Value("${everypay.password}")
    private String password;

    @Value("${everypay.base-url}")
    private String baseUrl;

    @Value("${everypay.customer-url}")
    private String customerUrl;


    public Order save(Long personId, List<OrderProductDto> orderProducts, String parcelMachine){
        Order order = new Order();
        order.setCreated(new Date());

        Person person = personRepository.findById(personId).orElseThrow();
        order.setPerson(person); //TODO autentimise tunnusest

        List<OrderRow> orderRows = new ArrayList<>();
        List<Product> dbProducts = new ArrayList<>();

        double sum = 0;
        for(OrderProductDto orderProductDto : orderProducts){
            Product dbProduct = productRepository.findById(orderProductDto.getProductId()).orElseThrow();

            if (dbProduct.getStock() < orderProductDto.getQuantity()) {
                throw new RuntimeException(String.format("Tooteid nimega %s on laos %d", dbProduct.getName(), dbProduct.getStock()));
            }

            dbProduct.setStock(dbProduct.getStock() - orderProductDto.getQuantity());
            dbProducts.add(dbProduct);
            OrderRow orderRow = new OrderRow();
            orderRow.setProduct(dbProduct);
            orderRow.setQuantity(orderProductDto.getQuantity());
            orderRows.add(orderRow);
            sum += dbProduct.getPrice() * orderProductDto.getQuantity();
        }

        productRepository.saveAll(dbProducts);
        order.setOrderRows(orderRows);
        order.setTotal(sum);
        order.setPaymentState(PaymentState.initial);
        order.setParchelMachine(parcelMachine);

        return orderRepository.save(order);
    }

    public double calculateOrderSum(List<Product> products){
        double sum = 0;
        for(Product product : products){
            sum += product.getPrice();
        }
        return sum;
    }

    public List<ParcelMachine> getParchelMachines(String country) {
        String url = "https://www.omniva.ee/locations.json";
        ParcelMachine[] response = restTemplate.exchange(url, HttpMethod.GET, null, ParcelMachine[].class).getBody();
        return Arrays.stream(response).filter(e -> e.getA0_NAME().equals(country.toUpperCase())).toList();
    }

    public PaymentLink pay(Long id, double total) {
        String url = baseUrl + "/payments/oneoff";

        EveryPayBody body = new EveryPayBody();
        body.setAccount_name("EUR3D1");
        body.setNonce("342567979jhu" + ZonedDateTime.now() + Math.random());
        body.setTimestamp(ZonedDateTime.now().toString());
        body.setAmount(total);
        body.setOrder_reference("hfgdtejf" + id);
        body.setCustomer_url(customerUrl);
        body.setApi_username(username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity(body, headers);

        EveryPayResponse response = restTemplate.exchange(url, HttpMethod.POST, entity, EveryPayResponse.class).getBody();
        PaymentLink paymentLink = new PaymentLink();
        paymentLink.setLink(response.getPayment_link());
        return paymentLink;
}

    public OrderPaid checkPayment(String orderReference, String paymentReference) {
        String url = baseUrl + "/payments/"+paymentReference+"?api_username="+username+"&detailed=false";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        HttpEntity entity = new HttpEntity(headers);

        EveryPayCheck response = restTemplate.exchange(url, HttpMethod.GET, entity, EveryPayCheck.class).getBody();
        OrderPaid orderPaid = new OrderPaid();
        orderPaid.setPaid(response.getPayment_state().equals("settled"));

        if (!orderReference.equals(response.getOrder_reference())) {
            throw new RuntimeException("Order reference does not match");
        }

        Order order = orderRepository.findById(Long.valueOf(orderReference.replace("hfgdtejf", ""))).orElseThrow();
        order.setPaymentState(PaymentState.valueOf(response.getPayment_state()));
        orderRepository.save(order); //save andmebaasi

        return orderPaid;
    }
}
