package ee.kristina.veebipood.controller;

import ee.kristina.veebipood.model.Supplier1Product;
import ee.kristina.veebipood.model.Supplier2Product;
import ee.kristina.veebipood.service.EmailService;
import ee.kristina.veebipood.service.SupplierService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private EmailService emailService;

    @GetMapping("supplier1")
    public List<Supplier1Product> getProductsFromSupplier1() {

        emailService.sendPlainText("noreply@gmail.com", "Tere", "sisu");
        return supplierService.getProductsFromSupplier1();
    }

    @GetMapping("supplier2")
    public List<Supplier2Product> getProductsFromSupplier2() throws MessagingException {
        emailService.sendHtml("noreply@gmail.com", "Tere", "<h1>Tere</h1><button>Vajuta</button>");
        return supplierService.getProductsFromSupplier2();
    }
}
