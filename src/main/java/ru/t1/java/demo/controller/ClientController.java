package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.service.ClientService;
import ru.t1.java.demo.util.ClientMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    /*//@LogException
    @LogDataSourceError
    //@Track
    @GetMapping(value = "/client")
    //@HandlingResult
    public void doSomething() throws IOException, InterruptedException {
        try {
            clientService.parseJson();
            Thread.sleep(1000L);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
        }
    }*/

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto client) {
        Client savedClient = clientService.createClient(ClientMapper.toEntity(client));
        return new ResponseEntity<>(ClientMapper.toDto(savedClient),
                                    HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return new ResponseEntity<>(clientService.findAll().stream().map(ClientMapper::toDto).toList(),
                                    HttpStatus.OK);
    }

    @GetMapping("/{clientUuid}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable UUID clientUuid) {
        try {
            return new ResponseEntity<>(ClientMapper.toDto(clientService.findByClientUuid(clientUuid)),
                                        HttpStatus.OK);
            
        } catch (ClientException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{clientUuid}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable UUID clientUuid, @RequestBody ClientDto clientDto) {
        try {
            Client updatedClient = clientService.updateClient(clientUuid, ClientMapper.toEntity(clientDto));
            return new ResponseEntity<>(ClientMapper.toDto(updatedClient),
                                        HttpStatus.OK);
        } catch (ClientException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    @DeleteMapping("/{clientUuid}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID clientUuid) {
        try {
            clientService.deleteClient(clientUuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ClientException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}