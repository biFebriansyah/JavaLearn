package com.java.learn.hibernate;

import com.java.learn.hibernate.modal.AccountTransaction;
import com.java.learn.hibernate.modal.BankAccount;

import javax.persistence.EntityManager;

import javax.persistence.EntityManagerFactory;

import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Hibernate {

    EntityManager em = null;

    public Hibernate () {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        em = emf.createEntityManager();
//        registerBankAccount("bukanebi", "12345");
//        getAll();
//        getWithScalar();
//        findById(1);
        addSaldo("12345", new BigDecimal(20000));
        em.close();
    }

    private void registerBankAccount(String ownerName, String accountNumber){

        em.getTransaction().begin();
        BankAccount b = new BankAccount();
        b.setAccountNumber(accountNumber);
        b.setOwnerName(ownerName);
        b.setSaldo(BigDecimal.ZERO);
        em.persist(b);
        em.getTransaction().commit();

    }

    private void findById(long id) {
        BankAccount b = em.find(BankAccount.class, id);
        if (b != null) {
            em.getTransaction().begin();
            b.setOwnerName("newOwener");
            em.persist(b);
            em.getTransaction().commit();
        }
    }

    private void getAll() {
        Query q = em.createQuery("from BankAccount order by id desc",
                BankAccount.class);
        List<BankAccount> bankAccounts = q.getResultList();

        for (BankAccount b : bankAccounts) {
            System.out.printf("%d - %s - %s -%f \n", b.getId(), b.getAccountNumber(), b.getOwnerName(), b.getSaldo());
        }
    }

    private void getWithScalar() {
        Query q = em.createQuery("select accountNumber, ownerName from BankAccount order by id desc");
        List<Object[]> bankAccounts = q.getResultList();
        int no = 0;
        for(Object[] b : bankAccounts) {
            System.out.printf("%d - %s - %s \n", no, b[no], b[no+1]);
            no++;
        }
    }

    private void addSaldo(String accountNumber, BigDecimal amount){

        Query q = em.createQuery("from BankAccount where accountNumber = :accountNumber", BankAccount.class)
                .setParameter("accountNumber", accountNumber);
        List<BankAccount> bankAccounts = q.getResultList();

        if(bankAccounts.size() > 0){

            em.getTransaction().begin();
            BankAccount b = bankAccounts.get(0);
            b.setSaldo(b.getSaldo().add(amount));
            em.persist(b);

            AccountTransaction at = new AccountTransaction();
            at.setAmount(amount);
            at.setTransactionDate(new Date());
            at.setTransactionName(AccountTransaction.KREDIT);
            at.setBankAccount(b);

            em.persist(at);
            em.getTransaction().commit();

        } else {

// account number was not found

        }

    }

    public static void main(String[] args) {
        new Hibernate();
    }
}
