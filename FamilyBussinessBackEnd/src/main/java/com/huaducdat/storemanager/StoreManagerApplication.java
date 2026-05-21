package com.huaducdat.storemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.huaducdat.storemanager")
public class StoreManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreManagerApplication.class, args);
    }
}
