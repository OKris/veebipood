package ee.kristina.veebipood.controller;

import ee.kristina.veebipood.dto.OrderProductDto;
import ee.kristina.veebipood.entity.Order;
import ee.kristina.veebipood.model.OrderPaid;
import ee.kristina.veebipood.model.ParcelMachine;
import ee.kristina.veebipood.model.PaymentLink;
import ee.kristina.veebipood.repository.OrderRepository;
import ee.kristina.veebipood.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("orders")
    public List<Order> findAll () {
        return orderRepository.findAll();
    }

    @GetMapping("my-orders")
    public List<Order> myOrders () {
        Long personId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return orderRepository.findAllByPersonId(personId);
    }

    //localhost:8080/orders?personId=
    //personId laheb automaatselt tokeni kaudu
    @PostMapping("orders")
    public PaymentLink save(@RequestParam String parcelMachine, @RequestBody List<OrderProductDto> orderProducts){
        Long personId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Order order = orderService.save(personId, orderProducts, parcelMachine);
        //orderService.pay(order.getId(), order.getTotal());
        return orderService.pay(order.getId(), order.getTotal());
    }

    @GetMapping("check-payment")
    public OrderPaid checkPayment(@RequestParam String orderReference, String paymentReference) {
        return orderService.checkPayment(orderReference, paymentReference);
    }

    @GetMapping("parcelmachines")
    public List<ParcelMachine> getParcelMachines(@RequestParam String country) {
        return orderService.getParchelMachines(country);
    }
}
