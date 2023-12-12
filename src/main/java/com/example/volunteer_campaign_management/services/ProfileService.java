package com.example.volunteer_campaign_management.services;

import com.example.volunteer_campaign_management.dtos.AccountDTO;
import com.example.volunteer_campaign_management.dtos.ProfileDTO;
import com.example.volunteer_campaign_management.entities.ProfileEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ProfileEntity profileById(int accountId);
    ProfileDTO getProfileById(int profileId);

    ProfileDTO updateProfile(int accountId, String firstName, String lastName, String email, String phone, String address, MultipartFile avatar);
}
