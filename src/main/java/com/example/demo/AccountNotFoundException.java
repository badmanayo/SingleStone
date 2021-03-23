package com.example.demo;

public class AccountNotFoundException extends  RuntimeException {
    AccountNotFoundException(Long id) {
        super("Could not find account " + id);
    }
}
