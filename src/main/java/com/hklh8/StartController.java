package com.hklh8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by GouBo on 2018/1/28.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class StartController {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StartController.class, args);
    }
}