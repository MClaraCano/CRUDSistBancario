package com.webclara.pruebaspring.api.controllers;

import com.webclara.pruebaspring.api.dtos.AccountDto;
import com.webclara.pruebaspring.application.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    private AccountService service;

    @Autowired
    public AccountController(AccountService service){
        this.service = service;
    }


    @GetMapping(value = "/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts(){
        List<AccountDto> accounts = service.getAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @GetMapping(value = "/accounts/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        AccountDto account = service.getAccountById(id);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @PostMapping(value = "/accounts")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAccount(dto));
    }

    @PutMapping(value = "/accounts/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto account) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateAccount(id, account));
    }

    @DeleteMapping(value = "/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteAccount(id));
    }


}