package ee.kristina.veebipood.repository;

import ee.kristina.veebipood.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByPersonId(Long personId);
    //List<Booking> findByCreatedBetween(Date createdStart, Date createdEnd);
}
