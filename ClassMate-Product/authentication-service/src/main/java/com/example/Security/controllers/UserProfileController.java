package com.example.Security.controllers;

import com.example.Security.dto.user.profile.*;
import com.example.Security.entities.Attachment;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.publisher.CreateUserPublisher;
import com.example.Security.service.UserProfileService;
import com.example.Security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserService userService;

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
    public ResponseEntity<GetUserProfileResponseDTO> getUserProfile(@PathVariable Long userId) {
        try {
            GetUserProfileResponseDTO responseDTO = service.getUserProfile(userId);
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
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/{nickname}")
    public ResponseEntity<List<UserProfileResponseDTO>> searchUserByNickname(
            @PathVariable("nickname") String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<UserProfileResponseDTO> users = userService.searchUserByNickname(nickname, page, size);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/search/findMultiple")
    public ResponseEntity<List<UserProfileResponseDTO>> findMulitpleUsers(@RequestParam("userId") List<Long> userIds){
        List<UserProfileResponseDTO> users = userService.findMultipleUsers(userIds);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/search/profileSearch/{userId}")
    public ResponseEntity<UserProfileResponseDTO> findUserProfileSearchById(@PathVariable("userId") Long userId){
        UserProfileResponseDTO userProfileResponseDTO = userService.findUserProfileSearchById(userId);
        return new ResponseEntity<>(userProfileResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/search/forumMembers/{forumId}")
    public ResponseEntity<List<UserProfileWithRoleDTO>> findForumMembers(@PathVariable("forumId") Long forumId,
                                                                         @RequestParam("q") String nicknameQuery){
        List<UserProfileWithRoleDTO> members = userService.searchUsersInForum(forumId, nicknameQuery);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserProfile(@PathVariable("userId") Long userId,
                                                  @ModelAttribute UserProfileUpdateDTO userProfileUpdateDTO){
        try {
            service.updateUserProfile(userId, userProfileUpdateDTO);
        } catch (IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceWithNumericValueDoesNotExistException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
