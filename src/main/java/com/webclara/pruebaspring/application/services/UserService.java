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
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    // Declaro una instancia del repositorio con @Autowired y sin la anotación
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;
    public UserService(UserRepository repository,AccountRepository accountRepository){
        this.userRepository = repository;
        this.accountRepository=accountRepository;
    }


    public List<UserDto> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::userMapToDto)
                .collect(Collectors.toList());
    }


    public UserDto getUserById(Long id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            throw new ChangeSetPersister.NotFoundException();
        }
        UserDto userDto = UserMapper.userMapToDto(user);
        return userDto;
    }


    public UserDto createUser(UserDto userDto) throws Exception {
        if (userDto.getUsername() == "" || userDto.getPassword() == ""
        || userDto.getUsername() == null || userDto.getPassword() == null){
            throw new NullPointerException();
        }
        return UserMapper.userMapToDto(userRepository.save(UserMapper.dtoToUser(userDto)));
    }

    public UserDto update(Long id, UserDto userDto) throws ChangeSetPersister.NotFoundException {

        User user = userRepository.findById(id).orElse(null);

        if (user != null) {

            User userActualizado = UserMapper.dtoToUser(userDto);
            //userActualizado.setAccounts(user.getAccounts()); //este

            List<Long> listaIdAc = userDto.getIdAccounts();

            if (listaIdAc != null) { // Verifica que la lista de cuentas no sea null
                List<Account> accountList = accountRepository.findAllById(listaIdAc);
                //List<Account> accountListFiltrada = accountList.stream() //este
                //        .filter(e -> !user.getAccounts().contains(e)) //este
                //               .collect(Collectors.toList()); //este
                //userActualizado.getAccounts().addAll(accountListFiltrada); //este
                userActualizado.setAccounts(accountList);
            }

            userActualizado.setId(user.getId());

            user = userRepository.save(userActualizado);

            return UserMapper.userMapToDto(user);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public String delete(Long id){
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
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

