package br.nom.dailton.pocresttemplateinterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class TokenProviderInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TokenProviderInterceptor.class);
    private final TokenProvider tokenProvider;
    public TokenProviderInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();

        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            headers.setBearerAuth(tokenProvider.getToken());
//            Instant expiresAt = tokenProvider.getExpiresAt();
//            if (expiresAt != null) {
//                headers.put("X-EXPIRES-AT", List.of(expiresAt.toString()));
//            }
        }

        //chain of responsability
        ClientHttpResponse response = execution.execute(request, body);

        if (response.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
            log.atError().log("VOLTOU 401 DA API");
            tokenProvider.invalidateToken();

            headers.setBearerAuth(tokenProvider.getToken());
            Instant expiresAt = tokenProvider.getExpiresAt();
            if (expiresAt != null) {
                headers.put("X-EXPIRES-AT", List.of(expiresAt.toString()));
            }
            response = execution.execute(request, body);
        }

        return response;
    }
}
