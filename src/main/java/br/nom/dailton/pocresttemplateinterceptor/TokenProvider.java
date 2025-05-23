package br.nom.dailton.pocresttemplateinterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

public class TokenProvider {
    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    public record TokenRequest(String clientId, String clientSecret) {}
    public record TokenResponse(String token, Instant expiresAt) {}
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private TokenResponse tokenResponse = null;

    public TokenProvider(String clientId, String clientSecret) {
        restTemplate = new RestTemplate();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Instant getExpiresAt() {
        return tokenResponse == null ? null : tokenResponse.expiresAt();
    }

    public void invalidateToken() {
        tokenResponse = null;
    }

    public String getToken() {
        if (!tokenExpired()) {
            log.atInfo().log("GETTING CURRENT CACHED TOKEN ...");
            return tokenResponse.token();
        }

        log.atInfo().log("GETTING TOKEN FROM TOKEN PROVIDER ...");
        var requestEntity = RequestEntity.post("http://localhost:9999/tokenprovider")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TokenRequest(clientId, clientSecret));

        ResponseEntity<TokenResponse> responseEntity = restTemplate.exchange(requestEntity, TokenResponse.class);

        tokenResponse = responseEntity.getBody();
        return tokenResponse.token();
    }

    boolean tokenExpired() {
//        return tokenResponse == null || Instant.now(Clock.systemUTC()).isAfter(tokenResponse.expiresAt());
        return tokenResponse == null;
    }
}
