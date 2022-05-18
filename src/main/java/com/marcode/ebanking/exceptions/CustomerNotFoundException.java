package com.marcode.ebanking.exceptions;

public class CustomerNotFoundException extends  Exception{
    public CustomerNotFoundException(String message){
        super(message);
    }
}
