package com.webclara.pruebaspring.api.controllers;

import com.webclara.pruebaspring.api.dtos.UserDto;
import com.webclara.pruebaspring.api.mappers.UserMapper;
import com.webclara.pruebaspring.application.services.UserService;
import com.webclara.pruebaspring.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }



    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> usuarios = service.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    /*
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) throws Exception {
        UserDto userDto = service.getUserById(id);
        if (userDto == null){
            throw new Exception("ERROR");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }
    }
     */

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) throws Exception {
        UserDto userDto = service.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto user) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, user));
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        String mensaje = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(mensaje);
    }

}

