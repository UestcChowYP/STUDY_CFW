package com.alageek.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.alageek.crowd.mapper")
@SpringBootApplication
public class MySQLCrowdMainClass {
    public static void main(String[] args) {
        SpringApplication.run(MySQLCrowdMainClass.class, args);
    }
}
