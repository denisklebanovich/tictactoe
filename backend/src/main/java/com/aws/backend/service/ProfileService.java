package com.aws.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.backend.sse.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Log4j2
@RequiredArgsConstructor
public class ProfileService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3client;
    private final GameService gameService;

    public void uploadProfileImage(MultipartFile file) {
        try {
            String username = gameService.getUsername();
            String key = username + ".png";
            var result = s3client.putObject(bucketName, key, file.getInputStream(), null);
            result.getContentMd5();
        } catch (IOException e) {
            log.error("Error uploading image to S3", e);
        }
    }

    public S3Object getProfileImage(String username) {
        try {
            String key = username + ".png";
            return s3client.getObject(bucketName, key);
        } catch (Exception e) {
            log.error("The image does not exist in S3", e.getMessage());
            return null;
        }
    }

}
