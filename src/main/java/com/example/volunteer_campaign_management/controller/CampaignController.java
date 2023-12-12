package com.example.volunteer_campaign_management.controller;

import com.example.volunteer_campaign_management.dtos.CampaignDTO;
import com.example.volunteer_campaign_management.dtos.RequestVolunteerDTO;
import com.example.volunteer_campaign_management.entities.CampaignEntity;
import com.example.volunteer_campaign_management.services.CampaignService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer-campaign-management/api/v1")
@AllArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping("/createCampaign")
    public CampaignEntity createNewCampaign(@RequestParam("name") String name,
                                            @RequestParam("start_date")
                                            @DateTimeFormat(pattern = "dd-MM-yyyy") Timestamp start_date,
                                            @RequestParam("end_date")
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") Timestamp end_date,
                                            @RequestParam("desc") String desc,
                                            @RequestParam("title") String title,
                                            @RequestParam("location") String location,
                                            @RequestParam("image") MultipartFile image,
                                            @RequestParam("currentStatus") int currentStatus) {
        return campaignService.createNewCampaign(name, start_date, end_date, desc, title, location, image, currentStatus);
    }

    @PutMapping("/updateCampaign/{id}")
    public CampaignDTO updateCampaign(@PathVariable(value = "id") int campaignId, @RequestBody CampaignDTO campaignDTO) {
        return campaignService.updateCampaign(campaignId, campaignDTO);
    }

    @GetMapping("/campaign/{id}")
    public CampaignDTO getCampaignById(@PathVariable(value = "id") int campaignId) {
        return campaignService.getCampaignById(campaignId);
    }

    @PutMapping("/campaign/updateCampaignStatus")
    public CampaignDTO updateCampaignStatus(@RequestBody CampaignDTO campaignDTO) {

        return campaignService.updateCampaignStatus(campaignDTO);
    }

    @GetMapping("/campaigns")
    public java.util.List<CampaignDTO> getAllCampaigns() {
        return campaignService.getAllCampaigns();
    }

    @GetMapping (value = {"campaign/searchCampaign","campaign/searchCampaign/{query}"})
    public java.util.List<CampaignDTO> searchCampaign(@PathVariable(value = "query") Optional<String> query){
        return campaignService.searchCampaign(query);
    }

}
