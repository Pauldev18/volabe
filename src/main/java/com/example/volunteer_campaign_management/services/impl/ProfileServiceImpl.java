package com.example.volunteer_campaign_management.services.impl;


import com.example.volunteer_campaign_management.dtos.AccountDTO;
import com.example.volunteer_campaign_management.dtos.ProfileDTO;
import com.example.volunteer_campaign_management.entities.AccountEntity;
import com.example.volunteer_campaign_management.entities.ProfileEntity;
import com.example.volunteer_campaign_management.mappers.MapperUtil;
import com.example.volunteer_campaign_management.repositories.AccountRepository;
import com.example.volunteer_campaign_management.repositories.ProfileRepository;
import com.example.volunteer_campaign_management.services.ProfileService;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final MapperUtil mapperUtil;

    public ProfileServiceImpl(ProfileRepository profileRepository, AccountRepository accountRepository, MapperUtil mapperUtil) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
        this.mapperUtil = mapperUtil;
    }

    static AccountDTO getAccountDTO(AccountDTO accountDTO, AccountEntity accountEntity, AccountRepository accountRepository) {
        if(accountDTO.getAvatar() != null && !accountDTO.getAvatar().isEmpty()){
            accountEntity.getProfileEntity().setAvatar(accountDTO.getAvatar());
        }
        if(accountDTO.getFirstname() != null && !accountDTO.getFirstname().isEmpty()){
            accountEntity.getProfileEntity().setFirstname(accountDTO.getFirstname());
        }
        if(accountDTO.getLastname() != null && !accountDTO.getLastname().isEmpty()){
            accountEntity.getProfileEntity().setLastname(accountDTO.getLastname());
        }
        if(accountDTO.getAddress() != null && !accountDTO.getAddress().isEmpty()){
            accountEntity.getProfileEntity().setAddress(accountDTO.getAddress());
        }
        accountRepository.save(accountEntity);
        return accountDTO;
    }

    @Override
    public ProfileEntity profileById(int accountId) {
        System.out.println(accountId);
        ProfileEntity profileEntity = profileRepository.findById(accountId).get();
        try {
            return profileEntity;
        } catch (Exception e) {
            e.getMessage();
            return  null;
        }
    }

    @Override
    public ProfileDTO getProfileById(int accountId) {
        try {
            AccountEntity accountEntity = accountRepository.findById(accountId).get();
            ProfileDTO profileDTO = mapperUtil.mapToProfileDTO(accountEntity);
            profileDTO.setFirstname(accountEntity.getProfileEntity().getFirstname());
            profileDTO.setLastname(accountEntity.getProfileEntity().getLastname());
            profileDTO.setAvatar(accountEntity.getProfileEntity().getAvatar());
            profileDTO.setAddress(accountEntity.getProfileEntity().getAddress());
            profileDTO.setPhone(accountEntity.getPhone());
            profileDTO.setEmail(accountEntity.getEmail());
            profileDTO.setRol(accountEntity.getRoleEntity().getName());
            profileDTO.setDepartment(accountEntity.getDepartmentEntity().getName());

            return profileDTO;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public AccountDTO updateProfile(int accountId, AccountDTO accountDTO) {
        try{
            AccountEntity accountEntity = accountRepository.findById(accountId).get();
            return getAccountDTO(accountDTO, accountEntity, accountRepository);
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }
}
