package com.example.volunteer_campaign_management.services;

import com.example.volunteer_campaign_management.dtos.StoryDTO;
import com.example.volunteer_campaign_management.entities.StoryEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StoryService {
    StoryEntity createNewStory(String name, String content, String title, MultipartFile image, int campaginID);
    Boolean deleteStory(int storyId);
    List<StoryDTO> getAllStory();

    StoryDTO getStoryById(int storyId);

    List<StoryDTO> searchStory(Optional<String> query);
    StoryDTO updateStory(int storyId, StoryDTO storyDTO);
}
