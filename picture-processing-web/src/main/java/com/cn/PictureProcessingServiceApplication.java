package com.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.cn.dao"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class PictureProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictureProcessingServiceApplication.class, args);
    }

}
