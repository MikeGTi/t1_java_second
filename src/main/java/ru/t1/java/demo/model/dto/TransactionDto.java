package ru.t1.java.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import ru.t1.java.demo.model.enums.TransactionStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link ru.t1.java.demo.model.Transaction}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto implements Serializable {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("account_uuid")
    private Long accountUuid;

    @JsonProperty("client_uuid")
    private Long clientUuid;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("status")
    private TransactionStatus status;

    @CreatedDate
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}