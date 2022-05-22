package com.marcode.ebanking;

import com.marcode.ebanking.dtos.BankAccountDTO;
import com.marcode.ebanking.dtos.CurrentAccountDTO;
import com.marcode.ebanking.dtos.CustomerDTO;
import com.marcode.ebanking.dtos.SavingAccountDTO;
import com.marcode.ebanking.entities.*;
import com.marcode.ebanking.enums.AccountStatus;
import com.marcode.ebanking.enums.OperationType;
import com.marcode.ebanking.exceptions.BalanceNotSufficientException;
import com.marcode.ebanking.exceptions.BankAccountNotFoundException;
import com.marcode.ebanking.exceptions.CustomerNotFoundException;
import com.marcode.ebanking.repositories.AccountOperationRepository;
import com.marcode.ebanking.repositories.BankAccountRepository;
import com.marcode.ebanking.repositories.CustomerRepository;
import com.marcode.ebanking.services.BankAccountService;
import com.marcode.ebanking.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerService customerService, BankAccountService bankAccountService){
		return args -> {
			Stream.of("John", "Snow", "Arya", "Stark").forEach(name ->{
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setName(name);
				customerDTO.setEmail(name+"@gmail.com");
				customerService.saveCustomer(customerDTO);
			});



			customerService.listCustomer().forEach(customer ->{
				try {
					bankAccountService.saveCurrentBankAccount(
							Math.random()*90000,
							9000,
							customer.getId(),
							AccountStatus.CREATED);
					bankAccountService.saveSavingBankAccount(
							Math.random()*90000,
							5.5,
							customer.getId(),
							AccountStatus.CREATED);
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});

			List<BankAccountDTO> bankAccountDTOs =  bankAccountService.bankAccountList();

			for(BankAccountDTO bankAccountTDO: bankAccountDTOs) {

				for(int i=0; i< 5; i++){
					String accountId;
					if(bankAccountTDO instanceof SavingAccountDTO){
						accountId = ((SavingAccountDTO)bankAccountTDO).getId();
					}else{
						accountId = ((CurrentAccountDTO)bankAccountTDO).getId();
					}
					bankAccountService.credit(accountId, 100000 + Math.random() * 120000, "Credit");
					bankAccountService.debit(accountId, 90000 + Math.random() * 9000, "Debit");
				}
			};

		};
	};
}
