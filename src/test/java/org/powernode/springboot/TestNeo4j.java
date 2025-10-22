package org.powernode.springboot;

import org.powernode.springboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNeo4j {
    @Autowired
    BookRepository bookRepository;
    public void test1(){

    }
}
