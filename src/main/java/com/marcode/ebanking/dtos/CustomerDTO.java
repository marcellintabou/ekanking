package com.marcode.ebanking.dtos;

import com.marcode.ebanking.entities.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;

}
