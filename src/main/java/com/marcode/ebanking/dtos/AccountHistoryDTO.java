package com.marcode.ebanking.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int size;
    private List<AccountOperationDTO> accountOperationDTOs;

}
