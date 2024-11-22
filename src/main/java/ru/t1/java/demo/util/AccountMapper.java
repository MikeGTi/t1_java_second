package ru.t1.java.demo.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccountMapper {
    private final ClientRepository clientRepository;
    private final TransactionMapper transactionMapper;

    public Account toEntity(AccountDto dto) throws ClientException {
        Long clientUuid = dto.getClientUuid();
        Optional<Client> client = clientRepository.findById(clientUuid);
        if (client.isEmpty()) {
            throw new ClientException(String.format("Client with id %s is not exists", clientUuid));
        }
        Account account = Account.builder()
                .accountUuid(dto.getAccountUuid())
                .client(client.get())
                .accountType(dto.getAccountType())
                .status(dto.getStatus())
                .balance(dto.getBalance())
                .frozenAmount(dto.getFrozenAmount())
                .build();

        Set<Transaction> transactionSet = new HashSet<>();
        if (dto.getTransactions() != null) {
            transactionSet = dto.getTransactions().stream()
                                                  .map(transactionMapper::toEntity)
                                                  .collect(Collectors.toSet());
        }
        account.setTransactions(transactionSet);
        transactionSet.forEach(transaction -> transaction.setAccount(account));
        return account;
    }

    public AccountDto toDto(Account entity) {
        return AccountDto.builder()
                .accountUuid(entity.getAccountUuid())
                .clientUuid(entity.getClient().getClientUuid())
                .accountType(entity.getAccountType())
                .status(entity.getStatus())
                .balance(entity.getBalance())
                .frozenAmount(entity.getFrozenAmount())
                .transactions(entity.getTransactions().stream()
                                                      .map(transactionMapper::toDto)
                                                      .collect(Collectors.toSet()))
                .build();
    }
}
