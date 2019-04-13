package com.customBean;

import org.springframework.stereotype.Component;

@Component
public class TestBean
{
    public String sayHello()
    {
        return "Hello from TestBean";
    }
}
