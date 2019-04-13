package com.example.testSpringBoot.beans;

import org.springframework.stereotype.Component;

@Component
public class Header {
    public String getHeader(){
        return "Добавить авто:";
    }
}
