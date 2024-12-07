package ru.t1.java.demo.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.model.dto.CheckResponse;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckWebClientTest {


    @Mock
    CheckWebClient checkWebClient;

    @Test
    void check() {
        UUID uuid = UUID.fromString("770dd3ef-fa3b-4c35-9ec0-e07dff3811c3");
        when(checkWebClient.check(uuid))
                .thenReturn(Optional.of(CheckResponse.builder()
                .blocked(false)
                .build()));

        assertThat(checkWebClient.check(uuid).get())
                .isEqualTo(CheckResponse.builder()
                        .blocked(false)
                        .build());

    }


}