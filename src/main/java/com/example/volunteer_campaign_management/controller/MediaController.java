package com.example.volunteer_campaign_management.controller;

import com.example.volunteer_campaign_management.dtos.MediaDTO;
import com.example.volunteer_campaign_management.services.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer-campaign-management/api/v1")
@AllArgsConstructor
public class MediaController {
    @Autowired
    private MediaService mediaService;
    @PostMapping("/media/create")
    public MediaDTO addMedia(@RequestBody MediaDTO mediaDTO) {
        return mediaService.addMedia(mediaDTO);
    }

    @PutMapping("/media/update/{mediaId}")
    public MediaDTO updateMedia(@PathVariable int mediaId, @RequestBody MediaDTO mediaDTO) {
        return mediaService.updateMedia(mediaId, mediaDTO);
    }

    @DeleteMapping("/media/delete/{mediaId}")
    public void deleteMedia(@PathVariable int mediaId) {
        mediaService.deleteMedia(mediaId);
    }

    @GetMapping("/media/getById/{mediaId}")
    public MediaDTO getMediaById(@PathVariable int mediaId) {
        return mediaService.getMediaById(mediaId);
    }

    @GetMapping("/media/list")
    public List<MediaDTO> getAllMedia() {
        return mediaService.getAllMedia();
    }

    @GetMapping (value = {"/media/search","/media/search/{query}"})
    public List<MediaDTO> searchMedia(@PathVariable(value = "query") Optional<String> query) {
        return mediaService.searchMedia(query);
    }

}
