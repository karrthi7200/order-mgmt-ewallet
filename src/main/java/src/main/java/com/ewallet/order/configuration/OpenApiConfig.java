package src.main.java.com.ewallet.order.configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI orderMgmtOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Order Management API")
                        .version("v1.0")
                        .description("APIs for order-mgmt microservice"));
    }
}
