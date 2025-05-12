package br.nom.dailton.pocresttemplateinterceptor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate theAPIRestTemplate(RestTemplateBuilder builder) {
        TokenProvider tokenProvider = new TokenProvider("cid", "csecret");
        TokenProviderInterceptor tokenProviderInterceptor = new TokenProviderInterceptor(tokenProvider);
        return builder
                .additionalInterceptors(tokenProviderInterceptor)
                .build();
    }
}
