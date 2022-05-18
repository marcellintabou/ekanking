package com.marcode.ebanking.services;

import com.marcode.ebanking.dtos.*;
import com.marcode.ebanking.entities.*;
import com.marcode.ebanking.enums.AccountStatus;
import com.marcode.ebanking.enums.OperationType;
import com.marcode.ebanking.exceptions.BalanceNotSufficientException;
import com.marcode.ebanking.exceptions.BankAccountNotFoundException;
import com.marcode.ebanking.exceptions.CustomerNotFoundException;
import com.marcode.ebanking.mappers.BankAccountMapperImpl;
import com.marcode.ebanking.repositories.AccountOperationRepository;
import com.marcode.ebanking.repositories.BankAccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{


    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;
    private  CustomerService customerService;


    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId, AccountStatus status) throws CustomerNotFoundException {
        Customer customer = customerService.findCustomer(customerId);

        CurrentAccount currentAccount =  new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverdraft(overdraft);
        currentAccount.setCustomer(customer);
        currentAccount.setStatus(status);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);

        return dtoMapper.fromCurrentAccount(savedCurrentAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId, AccountStatus status) throws CustomerNotFoundException {
        Customer customer = customerService.findCustomer(customerId);

        SavingAccount savingAccount =  new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        savingAccount.setStatus(status);
        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);

        return dtoMapper.fromSavingAccount(savedSavingAccount);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        }else{
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = findBankAccount(accountId);

        if(bankAccount.getBalance() < amount) {
            System.out.println("bankAccount.getBalance() = " + bankAccount.getBalance() + "\n amount = " + amount);
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount = findBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            }else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOS;
    }

    private BankAccount findBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        return bankAccount;
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations= accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(operation -> dtoMapper.fromAccountOperation(operation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {

        BankAccount bankAccount = findBankAccount(accountId);

        Page<AccountOperation> accountOperations =
                accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        List<AccountOperationDTO> accountHistoryDTOS = accountOperations
                .getContent()
                .stream()
                .map(operation -> dtoMapper.fromAccountOperation(operation))
                .collect(Collectors.toList());
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountOperationDTOs(accountHistoryDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());

        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }

}
