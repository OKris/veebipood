package ee.kristina.veebipood.controller;

import ee.kristina.veebipood.entity.Product;
import ee.kristina.veebipood.repository.ProductRepository;
import ee.kristina.veebipood.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheService cacheService;

    /*@GetMapping("hi")
    public String hello () {
        return "Hello World";
    }*/


    @GetMapping("products")
    public Page<Product> getProducts (@RequestParam(required = false) Long categoryId, Pageable pageable) {
        if(categoryId == null) {
            return productRepository.findByActiveTrue(pageable);
        } else {
            return productRepository.findByActiveTrueAndCategory_Id(categoryId, pageable);
        }
    }

    @GetMapping("products/admin")
    public List<Product> getAllProducts () {
        return productRepository.findByOrderById();
    }

    @PostMapping("products")
    public Product addProduct(@RequestBody Product product) {
        if(product.getId()!=null) {
            throw new RuntimeException("Cannot add product with id");
        }
        return productRepository.save(product);
    }

    // localhost:8080/products?id=
    /*@DeleteMapping("products")
    public List<Product> deleteProduct (@RequestParam Long id) {
        productRepository.deleteById(id);
        return productRepository.findAll();
    }*/

// localhost:8080/products/{id}
    @DeleteMapping("products/{id}")
    public List<Product> deleteProduct (@PathVariable Long id) throws ExecutionException {
        productRepository.deleteById(id);
        cacheService.deleteProduct(id);
        return productRepository.findAll();
    }


    @GetMapping("products/{id}")
    public Product getProduct (@PathVariable Long id) throws ExecutionException {
        //return productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        return cacheService.getProduct(id);
    }

    @PutMapping("products")
    public Product editProduct(@RequestBody Product product) throws ExecutionException {
        if(product.getId()==null) {
            throw new RuntimeException("Cannot edit product without id");
        }
        cacheService.updateProduct(product);
        return productRepository.save(product);
    }


}
