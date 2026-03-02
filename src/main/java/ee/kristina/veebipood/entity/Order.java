package ee.kristina.veebipood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date created;
    private double total;

    @ManyToOne //yhel inimesel mitu orderit
    private Person person;
    private String parchelMachine;

    private PaymentState paymentState;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderRow> orderRows;
}
