package src.main.java.com.ewallet.order.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;
}