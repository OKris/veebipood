package ee.kristina.veebipood.model;

import lombok.Data;

import java.util.Date;

@Data
public class Supplier1Category {
    private int id;
    private String name;
    private String slug;
    private String image;
    private Date creationAt;
    private Date updatedAt;
}
