package ru.t1.java.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;


/**
 * DTO for {@link ru.t1.java.demo.model.Account}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto implements Serializable {
    @JsonProperty("account_uuid")
    Long accountUuid;

    @JsonProperty("client_uuid")
    UUID clientUuid;

    @JsonProperty("account_type")
    AccountType accountType;

    @JsonProperty("status")
    AccountStatus status;

    @JsonProperty("balance")
    BigDecimal balance;

    @JsonProperty("frozen_amount")
    BigDecimal frozenAmount;

    @JsonProperty("transactions")
    Set<TransactionDto> transactions;
}