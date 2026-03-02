package ee.kristina.veebipood.repository;

import ee.kristina.veebipood.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {


    List<Product> findByCategory_Id(Long id);
    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByActiveTrueAndCategory_Id(Long id, Pageable pageable);

    List<Product> findByOrderById();
}
