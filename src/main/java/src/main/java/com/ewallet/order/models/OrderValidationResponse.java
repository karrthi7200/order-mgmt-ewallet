package src.main.java.com.ewallet.order.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderValidationResponse {

    private boolean valid;
    private String message;

}

