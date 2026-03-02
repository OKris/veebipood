package ee.kristina.veebipood.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.Date;

@Data
public class Supplier1Product {
    private int id;
    private String title;
    private String slug;
    private int price;
    private String description;
    private Supplier1Category category;
    private ArrayList<String> images;
    private Date creationAt;
    private Date updatedAt;
}
