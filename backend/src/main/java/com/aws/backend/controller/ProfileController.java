package com.aws.backend.controller;

import com.aws.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;


    @GetMapping("{username}/image")
    public ResponseEntity<String> getProfileImage(@PathVariable String username) {
        var s3Object = profileService.getProfileImage(username);
        if (s3Object == null) {
            return ResponseEntity.notFound().build();
        }
        String image = s3Object.getObjectContent().getHttpRequest().getURI().toString();
        return ResponseEntity.ok(image);
    }

    @PostMapping(path = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String uploadProfileImage(@RequestParam("file") MultipartFile file) {
        profileService.uploadProfileImage(file);
        return "Image uploaded successfully";
    }
}
