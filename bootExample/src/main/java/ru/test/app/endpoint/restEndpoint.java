package ru.test.app.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.test.app.entity.Customer;
import ru.test.app.service.CustomerService;

import java.util.List;

@RestController
public class restEndpoint {

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    CustomerService customerService;
    //    http://localhost:8088/bootExample/sayHello
    @GetMapping("/sayHello")
    public String sayHello(){
        return ("Hello from Boot Example. Profile: " + profile);
    }

    //    http://localhost:8088/bootExample/getCustomers
    @GetMapping(value="/getCustomers",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Customer> getAllCustomers(){
        return customerService.getCustomers() ;
    }
}
