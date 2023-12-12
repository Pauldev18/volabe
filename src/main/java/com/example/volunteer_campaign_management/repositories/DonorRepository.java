package com.example.volunteer_campaign_management.repositories;


import com.example.volunteer_campaign_management.entities.CampaignEntity;
import com.example.volunteer_campaign_management.entities.DonorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonorRepository extends JpaRepository<DonorEntity,Integer> {
    List<DonorEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Optional<String> query, Optional<String> query1);

    List<DonorEntity> findByCampaignEntity(CampaignEntity campaignEntity);
}
