package com.helpme.tickets.aws;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AWSConstants {

    @Getter
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
}
