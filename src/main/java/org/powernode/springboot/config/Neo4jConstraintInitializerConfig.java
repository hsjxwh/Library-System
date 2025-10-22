package org.powernode.springboot.config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@Configuration
public class Neo4jConstraintInitializerConfig {
    Logger logger = LoggerFactory.getLogger(Neo4jConstraintInitializerConfig.class);
    @Bean
    @Order(1)
    public CommandLineRunner constraintInitializer(Driver driver){
        return args -> {
            initializeConstraints(driver);
        };
    }

    private void initializeConstraints(Driver driver) {
        logger.info("正在进行neo4j的数据库约束配置");
        try (var session = driver.session()) {
            createAccountConstrains(session);
            createTypeConstraints(session);
            createAuthorConstraints(session);
            createBookConstraints(session);
            logger.info("neo4j数据库约束初始化成功");
        }catch(Exception e){
            logger.error("neo4j初始化失败：{}",e.getMessage());
        }
    }

    private void createAuthorConstraints(Session session) {
        session.run("CREATE CONSTRAINT author_unique_name FOR (n:Author) REQUIRE n.name IS UNIQUE");
        logger.info("作者唯一约束成功");
    }

    private void createBookConstraints(Session session) {
        session.run("CREATE CONSTRAINT book_unique_id_and_name FOR (n:Book) REQUIRE (n.id,n.title) IS NODE KEY");
        logger.info("书籍唯一约束成功");
    }

    private void createTypeConstraints(Session session) {
        session.run("CREATE CONSTRAINT type_unique FOR (n:Type) REQUIRE n.type IS UNIQUE)");
        logger.info("类型唯一约束成功");
    }

    private void createAccountConstrains(Session session) {
        session.run("CREATE CONSTRAINT account_id FOR (n:Account) REQUIRE n.id IS UNIQUE");
    }
}
