package ru.test.app.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.test.app.entity.Customer;
import ru.test.app.request.CarRq;
import ru.test.app.service.CustomerService;

import java.util.List;

@RestController
public class restEndpoint {

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    CustomerService customerService;
    //    http://localhost:8080/bootExample/sayHello
    @GetMapping("/sayHello")
    public String sayHello(){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return (username + ", hello from Boot Example. Your profile: " + profile);
    }

    //    http://localhost:8080/bootExample/getCustomers
    @GetMapping(value="/getCustomers",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Customer> getAllCustomers(){
        return customerService.getCustomers() ;
    }

        //    http://localhost:8080/bootExample/printCar?model=bmw&power=150&color=black
    @GetMapping("/printCar")
    public String printCar(CarRq car){
        return car.toString();
    }
}
