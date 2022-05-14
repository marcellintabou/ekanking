package com.marcode.ebanking;

import com.marcode.ebanking.entities.*;
import com.marcode.ebanking.enums.AccountStatus;
import com.marcode.ebanking.enums.OperationType;
import com.marcode.ebanking.repositories.AccountOperationRepository;
import com.marcode.ebanking.repositories.BankAccountRepository;
import com.marcode.ebanking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			/*Stream.of("TABOU", "MEBU", "TAMKO").forEach(name ->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach(customer ->{
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(customer);
				currentAccount.setOverdraft(9000L);
				bankAccountRepository.save(currentAccount);
			});

			customerRepository.findAll().forEach(customer ->{
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(customer);
				savingAccount.setInterestRate(5.7);
				bankAccountRepository.save(savingAccount);
			});
			*/
			bankAccountRepository.findAll().forEach(account ->{
				//System.out.println(account.getId());
				for(int i=0; i<10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random() * 10000);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(account);
					accountOperationRepository.save(accountOperation);
				}
			});
			BankAccount bankAccount = bankAccountRepository.findById("f671a75b-8b9e-4db5-9343-9e13e3d6c8c7").orElse(null);
			System.out.println("*************************");
			System.out.println(bankAccount.getId());
			System.out.println(bankAccount.getBalance());
			System.out.println(bankAccount.getStatus());
			System.out.println(bankAccount.getCreatedAt());
			System.out.println(bankAccount.getCustomer().getName());
			if(bankAccount instanceof  CurrentAccount){
				System.out.println("Rate=>" + ((CurrentAccount)bankAccount).getOverdraft());
			}else{
				System.out.println(((SavingAccount)bankAccount).getInterestRate());
			}
			bankAccount.getAccountOperations().forEach(op -> {
				System.out.println(op.getType()+ "\t" +op.getOperationDate() + "\t" + op.getAmount());
			});
		};
	};

}
