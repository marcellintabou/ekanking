package com.marcode.ebanking.exceptions;

public class BankAccountNotFoundException extends Exception{

    public BankAccountNotFoundException(String message){
        super(message);
    }
}
