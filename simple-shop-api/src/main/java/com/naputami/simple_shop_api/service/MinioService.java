package com.naputami.simple_shop_api.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import com.naputami.simple_shop_api.config.MinioConfig;

@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    public void uploadFile(String objectName, MultipartFile file)
            throws IOException, ServerException, InsufficientDataException,
            ErrorResponseException, XmlParserException, InternalException, InvalidKeyException,
            InvalidResponseException, NoSuchAlgorithmException {
        try {
            String bucketName = minioConfig.getBucketName();
            // Check if the bucket exists, if not create it
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            // Upload the file to the bucket
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            }
        } catch (MinioException e) {
            throw new RuntimeException("Error uploading file to MinIO", e);
        }
    }

    public void deleteFile(String objectName) throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
            InternalException {
        try {
            String bucketName = minioConfig.getBucketName();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (MinioException e) {
            throw new RuntimeException("Error deleting file from MinIO", e);
        }
    }

    public String getFilePublicUrl(String objectName) throws ServerException, InsufficientDataException, 
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, 
            XmlParserException, InternalException {
        try {
            String bucketName = minioConfig.getBucketName();
            int expiryTime = minioConfig.getPublicUrlExpiryTime();
            
            // Get presigned URL for the object
            String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expiryTime, TimeUnit.MINUTES)
                    .build());
            
            return url;
        } catch (MinioException e) {
            throw new RuntimeException("Error generating public URL for file from MinIO", e);
        }
    }

}