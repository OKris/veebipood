package ee.kristina.veebipood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price; // saab null olla
    // @ColumnDefault("false") // kui vaartust pole siis alguses vaja defineerida, hiljem pole vaja
    private boolean active; // ei saa null olla

    @ManyToOne
    private Category category;

    private String image;

    //@ColumnDefault("0") kui lisan hiljem siis korra vajalik
    private int stock;
}
