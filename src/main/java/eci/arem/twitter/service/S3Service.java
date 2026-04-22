package eci.arem.twitter.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${AWS_BUCKET}")
    private String bucketName;

    @Value("${AWS_REGION}")
    private String region;

    public S3Service(@Value("${AWS_REGION}") String region) {
        Region awsRegion = Region.of(region);
        this.s3Client = S3Client.builder().region(awsRegion).build();
        this.presigner = S3Presigner.builder().region(awsRegion).build();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
        return fileName;
    }

    public String generatePresignedUrl(String fileName) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(req -> req.bucket(bucketName).key(fileName))
                .build();
        return presigner.presignGetObject(presignRequest).url().toString();
    }
}