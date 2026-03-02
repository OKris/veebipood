package ee.kristina.veebipood.service;

import ee.kristina.veebipood.model.Supplier1Product;
import ee.kristina.veebipood.model.Supplier2Product;
import ee.kristina.veebipood.model.Supplier2Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SupplierService {

    @Autowired
    RestTemplate restTemplate;

    public List<Supplier1Product> getProductsFromSupplier1() {
        String url = "https://api.escuelajs.co/api/v1/products";
        Supplier1Product[] response = restTemplate.exchange(url, HttpMethod.GET, null, Supplier1Product[].class).getBody();
        return Arrays.stream(response).filter(e -> e.getCreationAt().equals(e.getCategory().getCreationAt())).toList();
    }

    public List<Supplier2Product> getProductsFromSupplier2() {
        String url = "https://api.itbook.store/1.0/search/spring?page=1";
        Supplier2Response response = restTemplate.exchange(url, HttpMethod.GET, null, Supplier2Response.class).getBody();
        return response.getBooks();

    }
}
