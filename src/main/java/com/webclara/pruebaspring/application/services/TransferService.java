package com.webclara.pruebaspring.application.services;

import com.webclara.pruebaspring.api.dtos.TransferDto;
import com.webclara.pruebaspring.api.mappers.TransferMapper;
import com.webclara.pruebaspring.application.exceptions.InsufficientFundsException;
import com.webclara.pruebaspring.domain.exceptions.AccountNotFoundException;
import com.webclara.pruebaspring.domain.exceptions.TransferNotFoundException;
import com.webclara.pruebaspring.domain.models.Account;
import com.webclara.pruebaspring.domain.models.Transfer;
import com.webclara.pruebaspring.infraestructure.repositories.AccountRepository;
import com.webclara.pruebaspring.infraestructure.repositories.TransfersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    @Autowired
    private TransfersRepository transfersRepository;

    @Autowired
    private AccountRepository accountRepository;
    private AccountService accountService;

    public TransferService(TransfersRepository repository){
        this.transfersRepository = repository;
    }


    public List<TransferDto> getTransfers(){
        List<Transfer> transfers = transfersRepository.findAll();
        return transfers.stream()
                .map(TransferMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public TransferDto getTransferById(Long id) throws ChangeSetPersister.NotFoundException {
        Transfer transfer = transfersRepository.findById(id).orElseThrow(() ->
                new ChangeSetPersister.NotFoundException());
        return TransferMapper.transferToDto(transfer);
    }



    @Transactional
    public TransferDto performTransfer(TransferDto dto) {
        // Comprobar si las cuentas de origen y destino existen
        Account originAccount = accountRepository.findById(dto.getOrigin())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getOrigin()));
        Account destinationAccount = accountRepository.findById(dto.getTarget())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getTarget()));

        // Comprobar si la cuenta de origen tiene fondos suficientes
        if (originAccount.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the account with id: " + dto.getOrigin());
        }

        // Realizar la transferencia
        originAccount.setBalance(originAccount.getBalance().subtract(dto.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(dto.getAmount()));

        // Guardar las cuentas actualizadas
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        // Crear la transferencia y guardarla en la base de datos
        Transfer transfer = new Transfer();
        // Creamos un objeto del tipo Date para obtener la fecha actual
        Date date = new Date();
        // Seteamos el objeto fecha actual en el transferDto
        transfer.setDate(date);
        transfer.setOrigin(originAccount.getId());
        transfer.setTarget(destinationAccount.getId());
        transfer.setAmount(dto.getAmount());
        transfer = transfersRepository.save(transfer);

        // Devolver el DTO de la transferencia realizada
        return TransferMapper.transferToDto(transfer);
    }







}