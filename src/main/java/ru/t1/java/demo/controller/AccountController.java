package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.service.impl.AccountServiceImpl;
import ru.t1.java.demo.service.impl.ClientServiceImpl;
import ru.t1.java.demo.util.AccountMapper;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/account")
public class AccountController {

    private final ClientServiceImpl clientServiceImpl;
    private final AccountServiceImpl accountServiceImpl;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        try {
            Account savedAccount = accountServiceImpl.create(accountMapper.toEntity(accountDto));
            return new ResponseEntity<>(accountMapper.toDto(savedAccount),
                                        HttpStatus.CREATED);
            } catch (ClientException e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return new ResponseEntity<>(accountServiceImpl.findAll().stream().map(accountMapper::toDto).toList(),
                                    HttpStatus.OK);
    }

    @GetMapping("/{accountUuid}")
    public ResponseEntity<AccountDto> getAccountByUuid(@PathVariable UUID accountUuid) {
        try {
            return new ResponseEntity<>(accountMapper.toDto(accountServiceImpl.findByUuid(accountUuid)), HttpStatus.OK);
        } catch (AccountException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{accountUuid}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable UUID accountUuid, @RequestBody AccountDto accountDto) {
        Client client;
        try {
            client = clientServiceImpl.findByClientUuid(accountDto.getAccountUuid());
        } catch (ClientException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Account account = accountServiceImpl.findByUuid(accountUuid);

            account.setAccountType(accountDto.getAccountType());
            account.setBalance(accountDto.getBalance());
            account.setClient(client);

            accountDto.getTransactions().stream()
                                        .map(transactionMapper::toEntity)
                                        .forEach(account::addTransaction);

            return new ResponseEntity<>(accountMapper.toDto(accountServiceImpl.create(account)), HttpStatus.OK);
        } catch (AccountException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{accountUuid}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountUuid) {
        try {
            accountServiceImpl.delete(accountUuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AccountException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/client/{clientUuid}")
    public ResponseEntity<List<AccountDto>> getAllAccountsByClientId(@PathVariable UUID clientUuid) {
        return new ResponseEntity<>(clientServiceImpl.findAccountsByClientUuid(clientUuid).stream()
                                                                                          .map(accountMapper::toDto)
                                                                                          .toList(),
                                    HttpStatus.OK);
    }
}
