package com.java.learn.hibernate.modal;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "account_transaction")
public class AccountTransaction {

    public static final String KREDIT = "KREDIT";
    public static final String DEBIT = "DEBIT";
    public static final String TRANSFER = "TRANSFER";

    @Id // value will be generated based on the strategy
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date", nullable = false, updatable = false)
    private Date transactionDate;

    @Column(name = "transaction_name", nullable = false)
    private String transactionName;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    BankAccount bankAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
