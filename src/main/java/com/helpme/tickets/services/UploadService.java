package com.helpme.tickets.services;

import com.helpme.tickets.aws.AWSConstants;
import com.helpme.tickets.aws.S3UploadService;
import com.helpme.tickets.exceptions.FileUploadException;
import com.helpme.tickets.utils.FileSizeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class UploadService {

    @Autowired
    S3UploadService s3UploadService;

    @Autowired
    AWSConstants awsConstants;

    public void uploadFile(MultipartFile file) {
        log.info("File received: {}, size: {}", file.getOriginalFilename(), FileSizeUtils.formatSize(file.getSize()));
        try {
            this.s3UploadService.uploadFile(file, awsConstants.getBucketName(), file.getOriginalFilename());
            log.info("File successfully uploaded");
        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new FileUploadException();
        }
    }

}
