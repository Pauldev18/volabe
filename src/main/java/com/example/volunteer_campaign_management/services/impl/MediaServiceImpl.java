package com.example.volunteer_campaign_management.services.impl;

import com.example.volunteer_campaign_management.dtos.MediaDTO;
import com.example.volunteer_campaign_management.entities.MediaEntity;
import com.example.volunteer_campaign_management.mappers.MapperUtil;
import com.example.volunteer_campaign_management.repositories.MediaRepository;
import com.example.volunteer_campaign_management.services.MediaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    private final MapperUtil mapperUtil;

    @Autowired
    private MediaRepository mediaRepository;

    public MediaServiceImpl(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public MediaDTO addMedia(MediaDTO mediaDTO) {
        MediaEntity mediaEntity = new MediaEntity();
        BeanUtils.copyProperties(mediaDTO, mediaEntity);
        MediaEntity savedEntity = mediaRepository.save(mediaEntity);
        MediaDTO savedDTO = new MediaDTO();
        BeanUtils.copyProperties(savedEntity, savedDTO);
        return savedDTO;
    }

    @Override
    public MediaDTO updateMedia(int mediaId, MediaDTO mediaDTO) {
        Optional<MediaEntity> optionalEntity = mediaRepository.findById(mediaId);
        if (optionalEntity.isPresent()) {
            MediaEntity existingEntity = optionalEntity.get();
            BeanUtils.copyProperties(mediaDTO, existingEntity);
            MediaEntity updatedEntity = mediaRepository.save(existingEntity);
            MediaDTO updatedDTO = new MediaDTO();
            BeanUtils.copyProperties(updatedEntity, updatedDTO);
            return updatedDTO;
        } else {
            // Handle entity not found
            return null;
        }
    }

    @Override
    public void deleteMedia(int mediaId) {
        mediaRepository.deleteById(mediaId);
    }

    @Override
    public MediaDTO getMediaById(int mediaId) {
        Optional<MediaEntity> optionalEntity = mediaRepository.findById(mediaId);
        if (optionalEntity.isPresent()) {
            MediaDTO mediaDTO = new MediaDTO();
            BeanUtils.copyProperties(optionalEntity.get(), mediaDTO);
            return mediaDTO;
        } else {
            // Handle entity not found
            return null;
        }
    }

    @Override
    public List<MediaDTO> getAllMedia() {
        List<MediaEntity> mediaEntities = mediaRepository.findAll();
        return mediaEntities.stream()
                .map(mediaEntity -> {
                    MediaDTO mediaDTO = new MediaDTO();
                    BeanUtils.copyProperties(mediaEntity, mediaDTO);
                    return mediaDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaDTO> searchMedia(Optional<String> keyword) {
        List<MediaEntity> mediaEntityDTOS = new ArrayList<>();
        try{
            if (!keyword.isPresent()) {
                return getAllMedia();
            }
            else {
                mediaEntityDTOS = mediaRepository.findByImageContainingIgnoreCaseOrVideoContainingIgnoreCase(keyword, keyword);

            }
        } catch (Exception e) {
            e.getMessage();
        }
        return mapperUtil.mapToListMediaDTO(mediaEntityDTOS);
    }
}
