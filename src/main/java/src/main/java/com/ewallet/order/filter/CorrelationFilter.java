package src.main.java.com.ewallet.order.filter;

import org.springframework.stereotype.Component;

import java.util.Optional;

// Filter to capture correlation id from incoming requests
@Component
public class CorrelationFilter implements org.springframework.web.server.WebFilter {
    @Override
    public reactor.core.publisher.Mono<Void> filter(org.springframework.web.server.ServerWebExchange exchange,
                                                    org.springframework.web.server.WebFilterChain chain) {
        String corrId = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Correlation-Id"))
                .orElse(java.util.UUID.randomUUID().toString());
        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put("X-Correlation-Id", corrId));
    }
}
