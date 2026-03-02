package ee.kristina.veebipood.dto;

import ee.kristina.veebipood.entity.Address;
import lombok.Data;

@Data
public class PersonSignupDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;

}
