package org.powernode.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = {"org.powernode.springboot.mapper"})
public class LibrarySystemApplication {
    //入口程序
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(LibrarySystemApplication.class, args);
    }

}
