package com.marcode.ebanking.services;

import com.marcode.ebanking.dtos.*;
import com.marcode.ebanking.entities.BankAccount;
import com.marcode.ebanking.entities.CurrentAccount;
import com.marcode.ebanking.entities.Customer;
import com.marcode.ebanking.entities.SavingAccount;
import com.marcode.ebanking.enums.AccountStatus;
import com.marcode.ebanking.exceptions.BalanceNotSufficientException;
import com.marcode.ebanking.exceptions.BankAccountNotFoundException;
import com.marcode.ebanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId, AccountStatus status) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId, AccountStatus status) throws CustomerNotFoundException;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();


    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
