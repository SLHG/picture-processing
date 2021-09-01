package com.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"com.cn.dao"}, sqlSessionTemplateRef = "sqlSessionTemplate")
@EnableScheduling
public class PictureProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictureProcessingServiceApplication.class, args);
    }

}
