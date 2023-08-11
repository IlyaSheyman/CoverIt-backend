package ru.practicum.springbootapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringBootAppTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
    }
}
