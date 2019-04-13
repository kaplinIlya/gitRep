package com.example.testSpringBoot;

import com.example.testSpringBoot.domain.Car;
import com.example.testSpringBoot.repository.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CarRepo carRepo;

    @GetMapping(value = "/findByModel/{model}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Car> findByModel(@PathVariable("model")String model){
        return carRepo.findByModel(model);
    }

    @GetMapping(value = "/findByFirm/{firm}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Car> getFirstByPower(@PathVariable("firm")String firm){
        return carRepo.findCarByFirm(firm);
    }
}
