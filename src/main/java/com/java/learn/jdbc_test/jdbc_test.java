package com.java.learn.jdbc_test;

import com.java.learn.hibernate.modal.BankAccount;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public class jdbc_test {

    EntityManager em = null;

    public jdbc_test () {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");

        while (true) {
            em = emf.createEntityManager();
            Scanner obj = new Scanner(System.in);
            System.out.println("SELECT OPTION BELOW \n 0. Exit \n 1. register new bank account \n 2. put saldo \n 3. transfer \n 4. Show all bank account ");
            String input = obj.nextLine();

            if (input.equals("0")) {
                break;

            } else if (input.equals("1")) {
                registerBankAccount();

            } else if (input.equals("2")) {
                putSaldo();

            } else if (input.equals("4")) {
                getAll();
            }
            em.close();
        }

    }

    private void registerBankAccount(){
        String ownerName = null;
        String accountNumber = null;
        Scanner obj = new Scanner(System.in);

        while (true) {

            if (ownerName == null) {
                System.out.println("Enter owener name \n 0. Exit");
                ownerName = obj.nextLine();

                if (ownerName.equals("0")) {
                    break;
                } else if (ownerName.equals("") || ownerName.isEmpty()) {
                    ownerName = null;
                    continue;
                }
            }
            else if (ownerName != null && accountNumber == null) {
                System.out.println("Enter accountNumber \n 0. Exit");
                accountNumber = obj.nextLine();

                if (accountNumber.equals("0")) {
                    break;

                } else if (accountNumber.isEmpty() || accountNumber.equals("")) {
                    accountNumber = null;
                    continue;

                } else if (!checkNumerik(accountNumber)) {
                    accountNumber = null;
                    System.out.println("Account number must be Number");
                    continue;

                } else if (checkAccountNumber(accountNumber)) {
                    accountNumber = null;
                    System.out.println("This accountNumber has already been taken. Please pick another accountNumber");
                    continue;
                }

            } else {
                em.getTransaction().begin();
                BankAccount b = new BankAccount();
                b.setAccountNumber(accountNumber);
                b.setOwnerName(ownerName);
                b.setSaldo(BigDecimal.ZERO);
                em.persist(b);
                em.getTransaction().commit();
                System.out.println("Register Succsess");
                break;
            }

        }

    }

    private void putSaldo () {
        String accountNumber = null;
        BigDecimal saldo = null;
        Scanner obj = new Scanner(System.in);

        while (true) {
            if (accountNumber == null) {
                System.out.println("Enter accountNumber \n 0. Exit");
                accountNumber = obj.nextLine();

                if (accountNumber.equals("0")) {
                    break;

                } else if (accountNumber.isEmpty() || accountNumber.equals("")) {
                    accountNumber = null;
                    continue;

                } else if (!checkAccountNumber(accountNumber)) {
                    accountNumber = null;
                    System.out.println("account Number not Found");
                    continue;

                } else if (!checkNumerik(accountNumber)) {
                    accountNumber = null;
                    System.out.println("Account number must be Number");
                    continue;

                }

            } else if (accountNumber != null && saldo == null) {
                System.out.println("Enter Saldo \n 0. Exit");
                String input = obj.nextLine();

                if (input.equals("0")) {
                    break;

                } else if (input.equals("") || input.isEmpty()) {
                    continue;

                } else if (!checkNumerik(input)) {
                    System.out.println("Saldo number must be Number");
                    continue;

                } else {
                    saldo = new BigDecimal(input);
                }

            }
            else {
                em.getTransaction().begin();
                Query q = em.createQuery("update BankAccount set saldo = :saldo where accountNumber = :accountNumber");
                q.setParameter("saldo", saldo);
                q.setParameter("accountNumber", accountNumber);
                int result = q.executeUpdate();
                em.getTransaction().commit();

                if (result == 1) {
                    System.out.println("Transaction Sucsess");
                    break;
                }
            }
        }
    }

    private Boolean checkAccountNumber(String accountNum) {

        Query q = em.createQuery("from BankAccount where accountNumber = :account ", BankAccount.class)
                .setParameter("account", accountNum);
        List<BankAccount> bankAccounts = q.getResultList();

        if (bankAccounts.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean checkNumerik (String number) {
        Boolean check = Pattern.matches("\\d*", number);
        return check;
    }

    private void getAll() {
        Query q = em.createQuery("from BankAccount order by id asc",
                BankAccount.class);
        List<BankAccount> bankAccounts = q.getResultList();

        for (BankAccount b : bankAccounts) {
            System.out.printf("%d - %s - %s -%f \n", b.getId(), b.getAccountNumber(), b.getOwnerName(), b.getSaldo());
        }
    }


    public static void main(String[] args) {
        new jdbc_test();
    }
}
