package com.example.volunteer_campaign_management.repositories;

import com.example.volunteer_campaign_management.entities.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Integer> {
    List<MediaEntity> findByImageContainingIgnoreCaseOrVideoContainingIgnoreCase(Optional<String> query1, Optional<String> query2);


}
