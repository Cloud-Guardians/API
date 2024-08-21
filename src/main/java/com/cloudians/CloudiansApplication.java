package com.cloudians;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "com.cloudians") // 패키지 지정
public class CloudiansApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudiansApplication.class, args);
    }

}
