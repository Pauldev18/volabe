package com.example.volunteer_campaign_management.services;

import com.example.volunteer_campaign_management.dtos.AccountDTO;
import com.example.volunteer_campaign_management.dtos.ProfileDTO;
import com.example.volunteer_campaign_management.entities.ProfileEntity;

public interface ProfileService {
    ProfileEntity profileById(int accountId);
    ProfileDTO getProfileById(int profileId);

    AccountDTO updateProfile(int accountId, AccountDTO accountDTO);
}
