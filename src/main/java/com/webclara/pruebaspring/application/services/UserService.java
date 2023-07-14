package com.webclara.pruebaspring.application.services;

import com.webclara.pruebaspring.api.dtos.AccountDto;
import com.webclara.pruebaspring.api.dtos.UserDto;
import com.webclara.pruebaspring.api.mappers.UserMapper;
import com.webclara.pruebaspring.application.exceptions.InsufficientFundsException;
import com.webclara.pruebaspring.application.exceptions.ServiceAdvice;
import com.webclara.pruebaspring.domain.exceptions.AccountNotFoundException;
import com.webclara.pruebaspring.domain.models.Account;
import com.webclara.pruebaspring.domain.models.User;
import com.webclara.pruebaspring.infraestructure.repositories.AccountRepository;
import com.webclara.pruebaspring.infraestructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    // Declaro una instancia del repositorio con @Autowired y sin la anotación
    @Autowired
    private UserRepository repository;

    @Autowired
    private AccountRepository accountRepository;
    public UserService(UserRepository repository,AccountRepository accountRepository){
        this.repository = repository;
        this.accountRepository=accountRepository;
    }


    public List<UserDto> getUsers(){
        List<User> users = repository.findAll();
        return users.stream()
                .map(UserMapper::userMapToDto)
                .collect(Collectors.toList());
    }

    /*
    public UserDto getUserById(Long id){
        User user = repository.findById(id).orElse(null);
        UserDto userDto = UserMapper.userMapToDto(user);
        return userDto;
    }
     */

    public UserDto getUserById(Long id) throws Exception {
        User user = repository.findById(id).orElse(null);
        if (user == null){
            throw new Exception();
        }
        UserDto userDto = UserMapper.userMapToDto(user);
        return userDto;
    }


    public UserDto createUser(UserDto user){
        return UserMapper.userMapToDto(repository.save(UserMapper.dtoToUser(user)));
    }

    public UserDto update(Long id, UserDto user){

        Optional<User> userCreated = repository.findById(id);

        if (userCreated.isPresent()){
            User entity = userCreated.get();

            User accountUpdated = UserMapper.dtoToUser(user);
            accountUpdated.setAccounts(entity.getAccounts());

            if (user.getIdAccounts() != null) { // Verifica que la lista de cuentas no sea null
                List <Account> accountList =accountRepository.findAllById(user.getIdAccounts());
                List<Account> accountListFilter=accountList.stream().filter(e->!entity.getAccounts().contains(e)).collect(Collectors.toList());
                accountUpdated.getAccounts().addAll(accountListFilter);
                accountUpdated.setAccounts(accountList);
            }

            accountUpdated.setId(entity.getId());

            User saved = repository.save(accountUpdated);

            return UserMapper.userMapToDto(saved);
        } else {
            throw new AccountNotFoundException("User not found with id: " + id);
        }
    }

    public String delete(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return "Se ha eliminado el usuario";
        } else {
            return "No se ha eliminado el usuario";
        }
    }

    // TODO: Generar la asociación de una primer cuenta al crear un User
    // Agregar una cuenta al usuario
    public UserDto addAccountToUser(AccountDto account, Long id){
        // primero: buscar el usuario por id
        // segundo: añadir la cuenta a la lista del usuario encontrado
        // tercero: devolver el usuario con la cuenta agregada
        return null;
    }

}

