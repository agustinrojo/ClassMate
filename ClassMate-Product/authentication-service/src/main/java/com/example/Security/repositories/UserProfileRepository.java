package com.example.Security.repositories;

import com.example.Security.entities.Attachment;
import com.example.Security.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<List<Attachment>> findProfilePhotoById(Long photoId);
}
