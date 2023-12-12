package com.example.volunteer_campaign_management.services.impl;

import com.example.volunteer_campaign_management.dtos.StoryDTO;
import com.example.volunteer_campaign_management.entities.CampaignEntity;
import com.example.volunteer_campaign_management.entities.RequestVolunteerEntity;
import com.example.volunteer_campaign_management.entities.StoryEntity;
import com.example.volunteer_campaign_management.mappers.MapperUtil;
import com.example.volunteer_campaign_management.repositories.CampaignRepository;
import com.example.volunteer_campaign_management.repositories.StoryRepository;
import com.example.volunteer_campaign_management.services.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final CampaignRepository campaignRepository;
    private final MapperUtil mapperUtil;

    @Override
    public StoryDTO createNewStory(StoryDTO storyDTO) {
        try {
            StoryEntity storyEntity = new StoryEntity();
            storyEntity.setStoryId(storyDTO.getStoryId());
            storyEntity.setName(storyDTO.getName());
            storyEntity.setContent(storyDTO.getContent());
            storyEntity.setTitle(storyDTO.getTitle());
            storyEntity.setCreated_at(storyDTO.getCreated_at());
            storyEntity.setImage(storyDTO.getImage());
            storyEntity.setCampaignEntity(campaignRepository.getOne(storyDTO.getCampaignId()));
            storyRepository.save(storyEntity);
            return storyDTO;
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    @Override
    public Boolean deleteStory(int storyId) {
        try{
            if(storyRepository.getOne(storyId) != null){
                storyRepository.deleteById(storyId);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.getMessage();
            return false;
        }
    }
    @Override
    public StoryDTO getStoryById(int storyId) {
        try{
            StoryEntity storyEntity = storyRepository.findById(storyId).get();
            StoryDTO storyDTO = new StoryDTO();
            storyDTO.setName(storyEntity.getName());
            storyDTO.setContent(storyEntity.getContent());
            storyDTO.setTitle(storyEntity.getTitle());
            storyDTO.setCreated_at(storyEntity.getCreated_at());
            storyDTO.setImage(storyEntity.getImage());
            storyDTO.setCampaignId(storyEntity.getCampaignEntity().getCampaignId());
            storyDTO.setCampaignName(storyEntity.getCampaignEntity().getName());
            storyDTO.setStoryId(storyId);
            return storyDTO;
        } catch (Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public List<StoryDTO> searchStory(Optional<String> query) {
        try {
            List<StoryEntity> storyEntities = new ArrayList<>();
            if (!query.isPresent()) {
                return  getAllStory();
            }
            storyEntities = storyRepository.findByNameContainsIgnoreCaseOrContentContainingIgnoreCaseOrTitleContainingIgnoreCase(query,query,query);
            List<CampaignEntity> campaignEntities = this.campaignRepository.findByNameContainsIgnoreCase(query);
            for (CampaignEntity campaignEntity : campaignEntities) {
                storyEntities.addAll(this.storyRepository.findByCampaignEntity(campaignEntity));
            }

            return mapperUtil.mapToListStory(storyEntities);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;

    }

    @Override
    public StoryDTO updateStory(int storyId, StoryDTO storyDTO) {
        try {
            StoryEntity storyEntity = storyRepository.getOne(storyId);
            storyEntity.setStoryId(storyDTO.getStoryId());
            storyEntity.setName(storyDTO.getName());
            storyEntity.setContent(storyDTO.getContent());
            storyEntity.setTitle(storyDTO.getTitle());
            storyEntity.setCreated_at(storyDTO.getCreated_at());
            storyEntity.setImage(storyDTO.getImage());
            storyEntity.setCampaignEntity(campaignRepository.getOne(storyDTO.getCampaignId()));
            storyRepository.save(storyEntity);
            return storyDTO;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public List<StoryDTO> getAllStory() {
        try {
            List<StoryEntity> storyEntities = storyRepository.findAll();
            List<StoryDTO> storyDTOS = mapperUtil.mapToListStoryDTO(storyEntities);
            return storyDTOS;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
