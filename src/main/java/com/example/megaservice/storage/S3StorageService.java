package com.example.megaservice.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.storage.s3.enabled", havingValue = "true")
public class S3StorageService implements StorageService {

    private final S3Client s3;
    private final String bucket;

    public S3StorageService(org.springframework.core.env.Environment env) {
        String region = env.getProperty("app.storage.s3.region", "us-east-1");
        this.bucket = env.getProperty("app.storage.s3.bucket", "changeme-bucket");
        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public String store(String prefix, String filename, String contentType, long size, InputStream data) throws IOException {
        String key = (prefix == null ? "" : prefix + "/") + UUID.randomUUID() + "-" + filename;
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType == null ? "application/octet-stream" : contentType)
                .build();
        s3.putObject(req, RequestBody.fromInputStream(data, size));
        return key;
    }

    @Override
    public InputStream load(String storageKey) throws IOException {
        GetObjectRequest req = GetObjectRequest.builder().bucket(bucket).key(storageKey).build();
        return s3.getObject(req);
    }

    @Override
    public void delete(String storageKey) throws IOException {
        DeleteObjectRequest req = DeleteObjectRequest.builder().bucket(bucket).key(storageKey).build();
        s3.deleteObject(req);
    }
}
