package ru.test.app.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class restEndpoint {

//    http://localhost:8088/bootExample/sayHello
    @GetMapping("/sayHello")
    public String sayHello(){
        return ("Hello from Boot Example");
    }
}
