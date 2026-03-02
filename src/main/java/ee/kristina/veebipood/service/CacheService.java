package ee.kristina.veebipood.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ee.kristina.veebipood.entity.Product;
import ee.kristina.veebipood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private ProductRepository productRepository;

    private final LoadingCache<Long, Product> loadingCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<>() {
                        @Override
                        public Product load(Long id) {
                            System.out.println("Loading product from DB: " + id);
                            return productRepository.findById(id).orElseThrow();
                        }
                    });

    public Product getProduct(Long id) throws ExecutionException {
        System.out.println("votan toodet...");
        return loadingCache.get(id);
    }

    public void deleteProduct(Long id) throws ExecutionException {
        loadingCache.invalidate(id);
    }

    public void updateProduct(Product product) throws ExecutionException {
        loadingCache.put(product.getId(), product);
    }
}
