package com.example.volunteer_campaign_management.services.impl;

import com.example.volunteer_campaign_management.dtos.CampaignDTO;
import com.example.volunteer_campaign_management.dtos.CurrentStatusDTO;
import com.example.volunteer_campaign_management.entities.CampaignEntity;
import com.example.volunteer_campaign_management.entities.CurrentStatusEntity;
import com.example.volunteer_campaign_management.mappers.MapperUtil;
import com.example.volunteer_campaign_management.repositories.CampaignRepository;
import com.example.volunteer_campaign_management.repositories.CurrentStatusRepository;
import com.example.volunteer_campaign_management.services.CampaignService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final CurrentStatusRepository currentStatusRepository;
    private CloudinaryService cloudinaryService;
    private final MapperUtil mapperUtil;

    @Override
    public CampaignEntity createNewCampaign(String name, Timestamp start_date, Timestamp end_date, String desc, String title, String location, MultipartFile image, int currentStatus) {
        try {
            CampaignEntity campaignEntity = new CampaignEntity();
            campaignEntity.setName(name);
            campaignEntity.setStart_date(start_date);
            campaignEntity.setEnd_date(end_date);
            campaignEntity.setDescription(desc);
            campaignEntity.setTitle(title);
            campaignEntity.setLocation(location);
            //set status_issue == current status = 1
            campaignEntity.setStatusIssueEntity(currentStatusRepository.getOne(currentStatus));
            campaignEntity.setImage(cloudinaryService.uploadImage(image));
            campaignRepository.save(campaignEntity);
            return campaignEntity;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public CampaignDTO updateCampaignStatus(CampaignDTO campaignDTO) {

        CampaignEntity campaignEntity = campaignRepository.findById(campaignDTO.getCampaignId()).get();
        try {
            campaignEntity.setStatus(campaignDTO.getStatus());
            campaignRepository.save(campaignEntity);
            return campaignDTO;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public List<CampaignDTO> getAllCampaigns() {
        List<CampaignDTO> campaignDTOS = new ArrayList<>();
        try {
            List<CampaignEntity> campaignEntities = campaignRepository.getAllCampaigns();
            campaignEntities.stream().forEach(campaignEntity -> {
                CampaignDTO campaignDTO = mapperUtil.mapCampaignEntityDTO(campaignEntity);
                campaignDTOS.add(campaignDTO);
            });
            return campaignDTOS;
        } catch (Exception e) {
            e.getMessage();
            return campaignDTOS;
        }
    }

    private void updateCurrentStatusAfterCreateCampaign(List<CurrentStatusDTO> currentStatusDTOS) {
        currentStatusDTOS.stream().forEach(currentStatusDTO -> {
            currentStatusDTO.setStatusId(1);
            CurrentStatusEntity currentStatusEntity = mapperUtil.mapDTOtoCurrentStatusEntity(currentStatusDTO);
            currentStatusRepository.save(currentStatusEntity);
        });
    }
    @Override
    public CampaignDTO getCampaignById(int campaignId) {
        try{
            CampaignEntity campaignEntity = campaignRepository.findById(campaignId).get();
            CampaignDTO campaignDTO = new CampaignDTO();
            campaignDTO.setName(campaignEntity.getName());
            campaignDTO.setStart_date(campaignEntity.getStart_date());
            campaignDTO.setEnd_date(campaignEntity.getEnd_date());
            campaignDTO.setDescription(campaignEntity.getDescription());
            campaignDTO.setTitle(campaignEntity.getTitle());
            campaignDTO.setLocation(campaignEntity.getLocation());
            campaignDTO.setStatusId(campaignEntity.getStatusIssueEntity().getStatusId());
            campaignDTO.setStatusName(campaignEntity.getStatusIssueEntity().getName());
            campaignDTO.setImage(campaignEntity.getImage());
            campaignDTO.setCampaignId(campaignId);
            return campaignDTO;
        } catch (Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public CampaignDTO updateCampaign(int campaignId, CampaignDTO campaignDTO) {
        try {
            CampaignEntity campaignEntity = campaignRepository.getOne(campaignId);
            campaignEntity.setName(campaignDTO.getName());
            campaignEntity.setStart_date(Timestamp.valueOf(campaignDTO.getStart_date().toLocalDateTime()));
            campaignEntity.setEnd_date(Timestamp.valueOf(campaignDTO.getEnd_date().toLocalDateTime()));
            campaignEntity.setDescription(campaignDTO.getDescription());
            campaignEntity.setTitle(campaignDTO.getTitle());
            campaignEntity.setLocation(campaignDTO.getLocation());
            campaignEntity.setStatusIssueEntity(currentStatusRepository.getOne(campaignDTO.getStatusId()));
            campaignEntity.setImage(campaignDTO.getImage());
            campaignRepository.save(campaignEntity);
            return campaignDTO;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public List<CampaignDTO> searchCampaign(Optional<String> query) {
        List<CampaignEntity> campaignDTOS = new ArrayList<>();
        try{
           if (!query.isPresent()) {
                return getAllCampaigns();
           }
            else {
                campaignDTOS = campaignRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCaseOrTitleContainsIgnoreCase(query,query, query, query);
                List<CurrentStatusEntity> currentStatusEntities = this.currentStatusRepository.findByNameContainingIgnoreCase(query);
               for (CurrentStatusEntity currentStatusEntity : currentStatusEntities) {
                   campaignDTOS.addAll(this.campaignRepository.findByStatusIssueEntity(currentStatusEntity));
               }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return mapperUtil.mapToListCampaignDTO(campaignDTOS);
    }
}
