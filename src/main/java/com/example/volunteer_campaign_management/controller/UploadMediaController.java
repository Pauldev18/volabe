package com.example.volunteer_campaign_management.controller;

import com.example.volunteer_campaign_management.dtos.editMediaDTO;
import com.example.volunteer_campaign_management.entities.CampaignEntity;
import com.example.volunteer_campaign_management.entities.MediaCampaignEntity;
import com.example.volunteer_campaign_management.repositories.CampaignRepository;
import com.example.volunteer_campaign_management.repositories.MediaCampaignRepository;
import com.example.volunteer_campaign_management.services.impl.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UploadMediaController {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private MediaCampaignRepository mediaCampaignRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public UploadMediaController(CampaignRepository campaignRepository, MediaCampaignRepository mediaCampaignRepository, CloudinaryService cloudinaryService) {
        this.campaignRepository = campaignRepository;
        this.mediaCampaignRepository = mediaCampaignRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/uploadMedia")
    public ResponseEntity<Object> uploadMedia(@RequestParam("idCampaign") int idCampagin,
                                              @RequestPart("images") MultipartFile image,
                                              @RequestPart("video") MultipartFile video
    )
    {
        try{
            CampaignEntity campaignEntity = campaignRepository.findByIdCom(idCampagin);
            if(campaignEntity != null){
                MediaCampaignEntity newObj = new MediaCampaignEntity();
                newObj.setCampaignEntity(campaignEntity);
                newObj.setImage(cloudinaryService.uploadImage(image));
                newObj.setVideo(cloudinaryService.uploadVideo(video));
                mediaCampaignRepository.save(newObj);
                return new ResponseEntity<>("thành cngô", HttpStatus.OK);
            }
            return new ResponseEntity<>("that bai", HttpStatus.BAD_REQUEST);
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/getByIdCampagin/{idCampagin}")
    public ResponseEntity<List<editMediaDTO>> getByIdCampagin(@PathVariable int idCampagin)
    {
        List<MediaCampaignEntity> mediaCampaignEntities = mediaCampaignRepository.findByCampagin(idCampagin);
        List<editMediaDTO> editMediaDTOS = mediaCampaignEntities.stream().map(mediaCampaignEntity -> {
            editMediaDTO editMediaDTO = new editMediaDTO();
            editMediaDTO.setImage(mediaCampaignEntity.getImage());
            editMediaDTO.setVideo(mediaCampaignEntity.getVideo());
            editMediaDTO.setId(mediaCampaignEntity.getMediaId());
            return editMediaDTO;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(editMediaDTOS, HttpStatus.OK);
    }

    @PostMapping("/editMedia/{idCampagin}/{idMedia}")
    public ResponseEntity<Object> editMedia(@PathVariable int idCampagin, @PathVariable int idMedia,
                                            @RequestParam(value = "newImage", required = false) MultipartFile newImage,
                                            @RequestParam(value = "newVideo", required = false) MultipartFile newVideo)
    {
        try{
            MediaCampaignEntity edit = mediaCampaignRepository.findByCampaginAndMedia(idCampagin, idMedia);
            if(newImage.isEmpty()&& newVideo.isEmpty()){
                return new ResponseEntity<>(edit, HttpStatus.OK);
            }
            else if(!newImage.isEmpty() && !newVideo.isEmpty()){
                edit.setVideo(cloudinaryService.uploadVideo(newVideo));
                edit.setImage(cloudinaryService.uploadImage(newImage));
                mediaCampaignRepository.save(edit);
                return new ResponseEntity<>(edit, HttpStatus.OK);
            }
            else if(!newImage.isEmpty()){
                edit.setImage(cloudinaryService.uploadImage(newImage));
                edit.setVideo(edit.getVideo());
                mediaCampaignRepository.save(edit);
                return new ResponseEntity<>(edit, HttpStatus.OK);
            }
            else if(!newVideo.isEmpty()){
                edit.setVideo(cloudinaryService.uploadVideo(newVideo));
                edit.setImage(edit.getImage());
                mediaCampaignRepository.save(edit);
                return new ResponseEntity<>(edit, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (IOException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}