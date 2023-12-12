package com.example.volunteer_campaign_management.services;

import com.example.volunteer_campaign_management.dtos.StoryDTO;

import java.util.List;
import java.util.Optional;

public interface StoryService {
    StoryDTO createNewStory(StoryDTO storyDTO);
    Boolean deleteStory(int storyId);
    List<StoryDTO> getAllStory();

    StoryDTO getStoryById(int storyId);

    List<StoryDTO> searchStory(Optional<String> query);
    StoryDTO updateStory(int storyId, StoryDTO storyDTO);
}
