package br.nom.dailton.pocresttemplateinterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public class TheAPIClient {
    private static final Logger log = LoggerFactory.getLogger(TheAPIClient.class);
    public record Payload(String message) {}
    private final RestOperations restOperations;
    public TheAPIClient(@Qualifier("theAPIRestTemplate") RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    public String getFirstMessage() {
        log.atInfo().log("GETTING MESSAGE ...");
        var requestEntity = RequestEntity.get("http://localhost:8080/the/api/1.0/hello")
//                .header(HttpHeaders.AUTHORIZATION, "thetoken")
                .build();
        ResponseEntity<Payload> responseEntity = restOperations.exchange(requestEntity, Payload.class);
        return responseEntity.getBody().message();
    }
}
