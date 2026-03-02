package ee.kristina.veebipood.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Supplier2Response {
    private String error;
    private String total;
    private String page;
    private ArrayList<Supplier2Product> books;
}
