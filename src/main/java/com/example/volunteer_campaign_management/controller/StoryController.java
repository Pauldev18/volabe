package com.example.volunteer_campaign_management.controller;

import com.example.volunteer_campaign_management.dtos.RequestVolunteerDTO;
import com.example.volunteer_campaign_management.dtos.StoryDTO;
import com.example.volunteer_campaign_management.entities.StoryEntity;
import com.example.volunteer_campaign_management.services.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/volunteer-campaign-management/api/v1")
@AllArgsConstructor
public class StoryController {
    private final StoryService storyService;
    @PostMapping("/createStory")
    public StoryEntity createNewStory(
            @RequestParam("name") String name,
            @RequestParam("content") String content,
            @RequestParam("title") String title,
            @RequestParam("image")MultipartFile image,
            @RequestParam("campaginID") int campaginID
            ) {
        return storyService.createNewStory(name, content, title, image, campaginID);
    }

    @PutMapping("/updateStory/{id}")
    public StoryDTO updateStory(@PathVariable(value = "id") int storyId, @RequestBody StoryDTO storyDTO) {
        return storyService.updateStory(storyId, storyDTO);
    }

    @GetMapping("/story/{id}")
    public StoryDTO getStoryById(@PathVariable(value = "id") int storyId){
        return storyService.getStoryById(storyId);
    }

    @GetMapping("/stories")
    public java.util.List<StoryDTO> getAllStory() {
        return storyService.getAllStory();
    }

    @DeleteMapping("/deleteStory/{id}")
    public boolean deleteStory(@PathVariable(value = "id") int storyId) {
        return storyService.deleteStory(storyId);
    }

    @GetMapping (value = {"stories/search","stories/search/{query}"})
    public java.util.List<StoryDTO> searchStory(@PathVariable(value = "query") Optional<String> query){
        return storyService.searchStory(query);
    }
}
