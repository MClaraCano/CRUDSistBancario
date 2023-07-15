package com.webclara.pruebaspring.application.services;

import com.webclara.pruebaspring.api.dtos.AccountDto;
import com.webclara.pruebaspring.api.dtos.UserDto;
import com.webclara.pruebaspring.api.mappers.AccountMapper;
import com.webclara.pruebaspring.domain.exceptions.AccountNotFoundException;
import com.webclara.pruebaspring.domain.models.Account;
import com.webclara.pruebaspring.domain.models.User;
import com.webclara.pruebaspring.infraestructure.repositories.AccountRepository;
import com.webclara.pruebaspring.infraestructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    // Declaro una instancia del repositorio con @Autowired y sin la anotación
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public List<AccountDto> getAccounts(){
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::AccountToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountDto createAccount(AccountDto accountDto){
        Long idOwner = accountDto.getOwner().getId();
        Optional<User> user = userRepository.findById(idOwner);
        User entidad = user.get();

        Account account = AccountMapper.dtoToAccount(accountDto);
        account.setOwner(entidad);
        account = accountRepository.save(account);

        AccountDto dto = AccountMapper.AccountToDto(account);
        return dto;
    }

    @Transactional
    public AccountDto getAccountById(Long id) throws ChangeSetPersister.NotFoundException {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null){
            throw new ChangeSetPersister.NotFoundException();
        } else {
            AccountDto accountDto = AccountMapper.AccountToDto(account);
            return accountDto;
        }
    }

    @Transactional
    public AccountDto updateAccount(Long id, AccountDto accountDto) throws ChangeSetPersister.NotFoundException {

        Account account = accountRepository.findById(id).orElse(null);

        if (account != null){
            if (accountDto.getAmount() !=  null){
                account.setBalance(accountDto.getAmount());
            }
            if (accountDto.getOwner() != null){
                Long idDto = accountDto.getOwner().getId();
                User user = userRepository.getReferenceById(idDto);
                if (user!=null){
                    account.setOwner(user);
                }
            }
            accountRepository.save(account);
            return AccountMapper.AccountToDto(account);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @Transactional
    public String deleteAccount(Long id){

        if (accountRepository.existsById(id)){
            accountRepository.deleteById(id);
            return "Se ha eliminado la cuenta";
        } else {
            return "El ID no existe; no se pudo eliminar la cuenta";
        }

    }



    // Métodos para usar en TRANSFER

    public Account buscarPorId(Long id) throws ChangeSetPersister.NotFoundException {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null){
            return account;
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }



    public BigDecimal egresarDinero(BigDecimal monto, Long id) throws ChangeSetPersister.NotFoundException {
        //1. Buscar cuenta por ID
        Account account = buscarPorId(id);
        //2. Recuperar el saldo de la cuenta
        BigDecimal saldo = account.getBalance();

        //3. Validar si hay dinero disponible en cuenta
        if (saldo.compareTo(monto) >= 0) {

            //4. Restar el monto al saldo de la cuenta
            BigDecimal nuevoSaldo = saldo.subtract(monto);

            //5. Devolvemor el nuevo saldo de la cuenta
            return nuevoSaldo;
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public BigDecimal ingresarDinero(BigDecimal monto, Long id) throws ChangeSetPersister.NotFoundException {
        //1. Buscar cuenta por ID
        Account account = buscarPorId(id);

        //2. Recuperar el saldo de la cuenta
        BigDecimal saldo = account.getBalance();

        //4. Sumar el monto al saldo de la cuenta
        BigDecimal nuevoSaldo = saldo.add(monto);

        //5. Devolvemor el nuevo saldo de la cuenta
        return nuevoSaldo;

    }




}

