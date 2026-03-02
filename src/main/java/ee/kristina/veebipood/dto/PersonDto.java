package ee.kristina.veebipood.dto;

import ee.kristina.veebipood.entity.Address;
import ee.kristina.veebipood.entity.Role;
import lombok.Data;

@Data
public class PersonDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;
        private Address address;
}
