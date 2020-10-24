package org.bargsten.di.impl;

import com.typesafe.config.Config;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.bargsten.di.api.FileService;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.regions.Region;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class S3Service implements FileService {
    final Config config;
    final S3Client s3;
    final String bucket;

    @Inject
    public S3Service(Config config) {
        this.config = config;
        bucket = config.getString("aws.s3.bucket");
        String region = config.getString("aws.s3.region");
        s3 = S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    @Override
    public List<String> list() {
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucket)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = List.ofAll(res.contents());
            return objects.map(S3Object::key);
        } catch (S3Exception ex) {
            log.error("got exception", ex);
            throw ex;
        }
    }

    @Override
    public String getContent(String path) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(path)
                .bucket(bucket)
                .build();

        // get the byte[] this AWS S3 object
        ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
        byte[] data = objectBytes.asByteArray();
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public InputStream getContentStream(String path) {
        return new ByteArrayInputStream(getContent(path).getBytes(StandardCharsets.UTF_8));
    }
}
