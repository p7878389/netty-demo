package com.shareworks.codeanalysis.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:12
 */
@SpringBootApplication
@EnableScheduling
public class CodeAnalysisClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeAnalysisClientApplication.class, args);
    }
}
