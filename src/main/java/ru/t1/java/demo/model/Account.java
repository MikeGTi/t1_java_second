package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_uuid")
    private Long accountUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_uuid")
    private Client client;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "frozen_amount", precision = 19, scale = 2)
    private BigDecimal frozenAmount;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setAccount(null);
    }
    
}
