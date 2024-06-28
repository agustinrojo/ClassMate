package com.example.Security.controllers;

import com.example.Security.dto.UserProfileRequestDTO;
import com.example.Security.dto.UserProfileResponseDTO;
import com.example.Security.entities.Attachment;
import com.example.Security.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileResponseDTO> createUserProfile(@ModelAttribute UserProfileRequestDTO requestDTO) {
        try {
            UserProfileResponseDTO responseDTO = service.createUserProfile(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable Long userId) {
        try {
            UserProfileResponseDTO responseDTO = service.getUserProfile(userId);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<ByteArrayResource> getProfilePhoto(@PathVariable Long photoId) {
        try {
            Attachment photo = service.getProfilePhotoById(photoId);
            ByteArrayResource resource = new ByteArrayResource(photo.getBytes());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(photo.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photo.getOriginalFilename() + "\"")
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
