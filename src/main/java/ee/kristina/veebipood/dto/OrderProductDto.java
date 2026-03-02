package ee.kristina.veebipood.dto;

import lombok.Data;

@Data
public class OrderProductDto {
    private Long productId;
    private int quantity;
}
