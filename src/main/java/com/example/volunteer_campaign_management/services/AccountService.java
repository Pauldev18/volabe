package com.example.volunteer_campaign_management.services;

import com.example.volunteer_campaign_management.dtos.AccountDTO;
import com.example.volunteer_campaign_management.entities.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountDTO> getAllAccounts();
    AccountDTO createNewAccount(AccountDTO accountDTO);
    AccountDTO getAccountById(int accountId);
    AccountDTO updateAccount(int accountId, AccountDTO updatedAccountDTO);
    AccountEntity save(AccountEntity accountEntity);
    void enableAccount(String email);
    void disableAccount(String email);

    List<AccountDTO> searchAccount(Optional<String> query);
}
