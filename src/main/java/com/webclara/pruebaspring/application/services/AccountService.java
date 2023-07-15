package com.webclara.pruebaspring.application.services;

import com.webclara.pruebaspring.api.dtos.AccountDto;
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
    public AccountDto getAccountById(Long id) throws ChangeSetPersister.NotFoundException {
        Account account = repository.findById(id).orElse(null);
        if (account == null){
            throw new ChangeSetPersister.NotFoundException();
        } else {
            AccountDto accountDto = AccountMapper.AccountToDto(account);
            return accountDto;
        }
    }

    @Transactional
    public AccountDto updateAccount(Long id, AccountDto account){

        Optional<Account> accountCreated = repository.findById(id);

        if (accountCreated.isPresent()){
            //me traigo la entidad de base de datos. esta es la que voy a modificar
            Account entity = accountCreated.get();
            //valido que informacion traigo en el request para solo modificar lo de la peticion y no toda la entidad
            if (account.getAmount()!=null){
                entity.setBalance(account.getAmount());
            }
            if (account.getOwner()!=null){
                //si hay un owner en el body me traigo la entidad completa de bd por el id para vinculale ese usuario a la cuenta
                User user=userRepository.getReferenceById(account.getOwner().getId());
                if (user!=null){
                    entity.setOwner(user);
                }

            }
            //no hay que usar este maper ya que te crea un account distinto al de bd y no tiene los valores del account en cuestion, hay que modificar solo el account traido de base de datos
            //Account accountUpdated = AccountMapper.dtoToAccount(account);

            Account saved = repository.save(entity);

            return AccountMapper.AccountToDto(saved);
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




}

