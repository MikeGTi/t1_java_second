package ru.t1.java.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.t1.java.demo.model.dto.CheckRequest;
import ru.t1.java.demo.model.dto.CheckResponse;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CheckWebClient extends BaseWebClient {

    @Value("${integration.resource}")
    private String resource;

    public CheckWebClient(WebClient webClient) {
        super(webClient);
    }

    public Optional<CheckResponse> check(UUID uuid) {
        log.debug("Старт запроса с uuid {}", uuid);
        ResponseEntity<CheckResponse> post;
        try {
            CheckRequest request = CheckRequest.builder()
                    .clientUuid(uuid)
                    .build();

            post = this.post(
                    uriBuilder -> uriBuilder.path(resource).build(),
                    request,
                    CheckResponse.class);


        } catch (Exception httpStatusException) {
            throw httpStatusException;
        }

        log.debug("Финиш запроса с uuid {}", uuid);
        return Optional.ofNullable(post.getBody());
    }
}
