package br.nom.dailton.pocresttemplateinterceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TokenProviderInterceptor implements ClientHttpRequestInterceptor {
    private final TokenProvider tokenProvider;
    public TokenProviderInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            headers.setBearerAuth(tokenProvider.getToken());
        }
        ClientHttpResponse response = execution.execute(request, body);
        //ja fiz uma chamada remota
        return response;
    }
}
