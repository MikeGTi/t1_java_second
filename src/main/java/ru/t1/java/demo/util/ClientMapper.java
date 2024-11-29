package ru.t1.java.demo.util;

import org.springframework.stereotype.Component;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.Client;

import java.util.UUID;

@Component
public class ClientMapper {

    public static Client toEntity(ClientDto dto) {
        if (dto.getFirstName() == null ||
                dto.getLastName() == null) {
            throw new ClientException("Client first/last name is required");
        }
        return Client.builder()
                .clientUuid(dto.getClientUuid())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .build();
    }

    public static ClientDto toDto(Client entity) {
        return ClientDto.builder()
                .clientUuid(entity.getClientUuid())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .build();
    }

    public static Client toEntityWithGeneratedUuid(ClientDto dto) {
        if (dto.getFirstName() == null ||
                dto.getLastName() == null) {
            throw new ClientException("Client first/last name is required");
        }
        return Client.builder()
                .clientUuid(UUID.randomUUID())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .build();
    }

    /*public static ClientDto toDtoWithUuid(Client entity) {
        return ClientDto.builder()
                .clientUuid(entity.getClientUuid())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .build();
    }*/

}
