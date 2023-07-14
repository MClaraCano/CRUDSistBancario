package com.webclara.pruebaspring.api.mappers;

import com.webclara.pruebaspring.api.dtos.AccountDto;
import com.webclara.pruebaspring.api.dtos.UserDto;
import com.webclara.pruebaspring.domain.models.Account;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {

    public Account dtoToAccount(AccountDto dto){
        Account account = new Account();
        account.setBalance(dto.getAmount());
        //account.setOwner(dto.getOwner());
        return account;
    }

    public AccountDto AccountToDto(Account account){
        AccountDto dto = new AccountDto();
        dto.setAmount(account.getBalance());
        UserDto userDto=UserMapper.userMapToDto(account.getOwner());
        dto.setOwner(userDto);
        dto.setId(account.getId());
        return dto;
    }
}

