package com.javiertorres.StarlingAPIChallenge.infrastructure.config;

import com.javiertorres.StarlingAPIChallenge.domain.exception.StarlingClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class StarlingWebClientConfig {

    @Bean
    public WebClient starlingWebClient(
        @Value("${starling.base.url}") String baseUrl,
        @Value("${starling.authorization.bearer}") String authorizationBearer) {
        ExchangeFilterFunction errorResponseFilter = ExchangeFilterFunction
            .ofResponseProcessor(StarlingWebClientConfig::exchangeFilterResponseProcessor);

        return WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultHeader(AUTHORIZATION, authorizationBearer)
            .defaultHeader(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .filter(errorResponseFilter)
            .build();
    }

    private static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.is4xxClientError() || status.is5xxServerError()) {
            return response.bodyToMono(String.class)
                .flatMap(body -> Mono.error(new StarlingClientException(body)));
        }

        return Mono.just(response);
    }
}
