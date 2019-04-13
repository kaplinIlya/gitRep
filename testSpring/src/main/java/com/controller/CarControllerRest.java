package com.controller;

import com.model.Car;
import com.service.CarServiceREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value ="/carService")
public class CarControllerRest {

    @Autowired
    @Qualifier(value = "carServiceREST")
    private CarServiceREST carServiceREST;

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Car> getCar(@PathVariable("id") long id){
        if (id == 0){
            return new ResponseEntity<Car>(HttpStatus.BAD_REQUEST);
        }
        Car newCar = carServiceREST.getByID(id);
        if (newCar == null){
            return new ResponseEntity<Car>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return  new ResponseEntity<Car>(newCar,httpHeaders,HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Car> saveCar(@RequestBody @Valid Car newCar){
        HttpHeaders httpHeaders = new HttpHeaders();
        if (newCar==null){
            return  new ResponseEntity<Car>(HttpStatus.BAD_REQUEST);
        }
        carServiceREST.add(newCar);
        return new ResponseEntity<Car>(newCar, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Car> deleteCar(@PathVariable("id") long id){
        if (id == 0){
            return new ResponseEntity<Car>(HttpStatus.BAD_REQUEST);
        }
        Car car = carServiceREST.getByID(id);
        if (car == null){
            return new ResponseEntity<Car>(HttpStatus.NOT_FOUND);
        }
        carServiceREST.delete(id);
        return new ResponseEntity<Car>(HttpStatus.OK);
        }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Car>> getAllCar(){
        List<Car> cars = carServiceREST.getAll();
        HttpHeaders httpHeaders = new HttpHeaders();
        if (cars == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Car>>(cars,HttpStatus.OK);
    }
}
