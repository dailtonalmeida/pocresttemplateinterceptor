package br.nom.dailton.pocresttemplateinterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class TokenProvider {
    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private String token = null;
    private Instant lastTokenTimestamp = null;

    public TokenProvider(String clientId, String clientSecret) {
        restTemplate = new RestTemplate();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getToken() {
        if (!tokenExpired()) {
            log.atInfo().log("GETTING CURRENT CACHED TOKEN ...");
            return token;
        }

        log.atInfo().log("GETTING TOKEN FROM TOKEN PROVIDER ...");
        var requestEntity = RequestEntity.post("http://localhost:9999/tokenprovider")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("clientId", clientId, "clientSecret", clientSecret));
        var responseType = new ParameterizedTypeReference<Map<String, String>>() {};

        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(requestEntity, responseType);

        token = responseEntity.getBody().get("token");
        lastTokenTimestamp = Instant.now();
        return token;
    }

    boolean tokenExpired() {
        return token == null || Instant.now().minusSeconds(5L).isAfter(lastTokenTimestamp);
    }
}
