package com.webclara.pruebaspring.api.mappers;

import com.webclara.pruebaspring.api.dtos.UserDto;
import com.webclara.pruebaspring.domain.models.Account;
import com.webclara.pruebaspring.domain.models.User;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    // Los mappers me permiten enviar los datos desde una entidad
    //  hacia un dto o visceversa


    public User dtoToUser(UserDto dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    public UserDto userMapToDto(User user){
        UserDto dto = new UserDto();
        List<Long> accountsId=new ArrayList<>();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        if (user.getAccounts()!=null)
            for (Account a:
                    user.getAccounts()) {
                Long id= a.getId();
                accountsId.add(id);
            }

        dto.setIdAccounts(accountsId);
        dto.setId(user.getId());
        return dto;
    }

}

