package ee.kristina.veebipood.model;

import lombok.Data;

@Data
public class AuthToken {
    private String token;
    private Long expiration;
}
