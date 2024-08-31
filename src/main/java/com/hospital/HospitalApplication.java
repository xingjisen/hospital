package com.hospital;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jason
 */
@Slf4j
@SpringBootApplication
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
        log.info("项目启动成功!🎆( ´･･)ﾉ(._.`)🎉");
    }

}
