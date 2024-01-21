package com.project.trackfit;

import com.project.trackfit.aws.S3Buckets;
import com.project.trackfit.aws.S3Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

/*    @Bean
    CommandLineRunner runner(S3Service s3Service, S3Buckets s3Buckets) {
        return args -> testBucket(s3Service, s3Buckets);
    }

    private static void testBucket(S3Service s3Service, S3Buckets s3Buckets) {
        s3Service.putObject(
                s3Buckets.getCustomer(),
                "foo",
                "Hello World".getBytes()
        );

        byte[] object = s3Service.getObject(
                s3Buckets.getCustomer(),
                "foo"
        );

        System.out.println("Hooray " + new String(object));
    }*/
}
