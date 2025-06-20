package com.snow.audit.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/1 17:44
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloServiceTest {

    @Autowired
    private HelloService helloService;

    /*@Before
    public void setUp() {
        //添加Mock注解初始化
        MockitoAnnotations.initMocks(this);
    }*/

    @Test
    public void testHello() {
        System.out.println(helloService.eatFood("mc"));
    }
}