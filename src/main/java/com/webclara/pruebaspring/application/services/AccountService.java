package com.webclara.pruebaspring.application.services;

import com.webclara.pruebaspring.api.dtos.AccountDto;
import com.webclara.pruebaspring.api.mappers.AccountMapper;
import com.webclara.pruebaspring.domain.exceptions.AccountNotFoundException;
import com.webclara.pruebaspring.domain.models.Account;
import com.webclara.pruebaspring.domain.models.User;
import com.webclara.pruebaspring.infraestructure.repositories.AccountRepository;
import com.webclara.pruebaspring.infraestructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public List<AccountDto> getAccounts(){
        List<Account> accounts = repository.findAll();
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
        account = repository.save(account);

        AccountDto dto = AccountMapper.AccountToDto(account);
        return dto;
    }

    @Transactional
    public AccountDto getAccountById(Long id) {
        AccountDto account = AccountMapper.AccountToDto(repository.findById(id).get()); //O: .findById(id).orElse(null);
        return account;
    }

    @Transactional
    public AccountDto updateAccount(Long id, AccountDto accountDto){

        Optional<Account> accountFoundById = repository.findById(id);

        if (accountFoundById.isPresent()){
            Account entity = accountFoundById.get();

            Account accountModificada = AccountMapper.dtoToAccount(accountDto);

            accountModificada.setId(entity.getId());

            Account guardada = repository.save(accountModificada);

            return AccountMapper.AccountToDto(guardada);
        } else {
            throw new AccountNotFoundException("Account not found with id: " + id);
        }

    }

    @Transactional
    public String deleteAccount(Long id){

        if (repository.existsById(id)){
            repository.deleteById(id);
            return "Se ha eliminado la cuenta";
        } else {
            return "El ID no existe; no se pudo eliminar la cuenta";
        }

    }

    // Agregar métodos de ingreso y egreso de dinero y realizacion de transferencia
    public BigDecimal withdraw(BigDecimal amount, Long idOrigin){
        // primero: Obtenemos la cuenta
        Account account = repository.findById(idOrigin).orElse(null);
        // segundo: debitamos el valor del amount con el amount de esa cuenta (validar si hay dinero disponible)
        if (account.getBalance().subtract(amount).intValue() > 0){
            account.setBalance(account.getBalance().subtract(amount));
            repository.save(account);
        }
        // tercero: devolvemos esa cantidad
        return account.getBalance().subtract(amount);
    }

    public BigDecimal addAmountToAccount(BigDecimal amount, Long idOrigin){
        // primero: Obtenemos la cuenta
        Account account = repository.findById(idOrigin).orElse(null);
        // segundo: acreditamos el valor del amount con el amount de esa cuenta
        account.setBalance(account.getBalance().add(amount));
        repository.save(account);
        // tercero: devolvemos esa cantidad
        return amount;
    }



}

