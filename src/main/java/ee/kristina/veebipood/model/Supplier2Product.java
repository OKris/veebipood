package ee.kristina.veebipood.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.Date;

@Data
public class Supplier2Product {
    private String title;
    private String subtitle;
    private String isbn13;
    private String price;
    private String image;
    private String url;
}
