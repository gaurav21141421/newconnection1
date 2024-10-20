package com.example.springbootaws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.nio.file.Path;

@Service
public class S3ServiceConfig {

    private final S3Client s3Client;
    private final String bucketName;

    public S3ServiceConfig(@Value("${aws.s3.bucket.name}") String bucketName,
                           @Value("${aws.access.key.id}") String accessKeyId,
                           @Value("${aws.secret.access.key}") String secretAccessKey,
                           @Value("${aws.region}") String region) {
        this.bucketName = bucketName;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region))
                .build();
    }

    public PutObjectResponse uploadFile(Path filePath) {
        File file = filePath.toFile();
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getName())
                .build();
        return s3Client.putObject(putRequest, filePath);
    }
}
