package com.marcode.ebanking.dtos;

import lombok.Data;
import lombok.ToString;

@Data
public class TransferRequestDTO {
    private String accountSource;
    private String accountDestination;
    private double amount;
    private String description;
}
