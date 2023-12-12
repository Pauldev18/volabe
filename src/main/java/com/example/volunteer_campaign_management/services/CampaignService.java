package com.example.volunteer_campaign_management.services;

import com.example.volunteer_campaign_management.dtos.CampaignDTO;
import com.example.volunteer_campaign_management.entities.CampaignEntity;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface CampaignService {
    CampaignEntity createNewCampaign(String name, Timestamp start_date, Timestamp end_date, String desc, String title, String location, MultipartFile image, int currentStatus);

    CampaignDTO updateCampaignStatus(CampaignDTO campaignDTO);

    List<CampaignDTO> getAllCampaigns();
    List<CampaignDTO> searchCampaign(Optional<String> query);

    CampaignDTO getCampaignById(int campaignId);
    CampaignDTO updateCampaign(int campaignId, CampaignDTO campaignDTO);
}
