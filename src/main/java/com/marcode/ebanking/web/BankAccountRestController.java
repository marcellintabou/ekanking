package com.marcode.ebanking.web;

import com.marcode.ebanking.dtos.*;
import com.marcode.ebanking.exceptions.BalanceNotSufficientException;
import com.marcode.ebanking.exceptions.BankAccountNotFoundException;
import com.marcode.ebanking.exceptions.CustomerNotFoundException;
import com.marcode.ebanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/ebanking/api/v1/accounts")
@CrossOrigin()
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @PostMapping()
    public BankAccountDTO saveBankAccount(@RequestBody BankAccountDTO bankAccountDTO) throws CustomerNotFoundException {
        if(bankAccountDTO instanceof CurrentAccountDTO){
            CurrentAccountDTO currentAccountDTO = (CurrentAccountDTO) bankAccountDTO;
            return bankAccountService.saveCurrentBankAccount(
                    currentAccountDTO.getBalance(),
                    currentAccountDTO.getOverdraft(),
                    currentAccountDTO.getCustomerDTO().getId(),
                    currentAccountDTO.getStatus());
        }else{
            SavingAccountDTO savingAccountDTO = (SavingAccountDTO) bankAccountDTO;
            return bankAccountService.saveSavingBankAccount(
                    savingAccountDTO.getBalance(),
                    savingAccountDTO.getInterestRate(),
                    savingAccountDTO.getCustomerDTO().getId(),
                    savingAccountDTO.getStatus());
        }

    }

    @GetMapping()
    public List<BankAccountDTO> listAccounts() throws BankAccountNotFoundException {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {

        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0" ) int page,
            @RequestParam(name="size",defaultValue = "1" ) int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/credit")
    public CreditDTO debit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/transfer")
    public TransferRequestDTO debit(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount()
        );
        return transferRequestDTO;
    }
}
