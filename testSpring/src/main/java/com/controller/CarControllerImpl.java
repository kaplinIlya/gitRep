package com.controller;

import com.model.Car;
import com.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarControllerImpl implements CarController {

    @Autowired
    private CarService carService;

    @Override
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        Car newCar = new Car();
        model.addAttribute("carJSP",newCar);
        return "addCar";
    }

    @RequestMapping(value = "addCar", method = RequestMethod.POST)
    public String addCar(@ModelAttribute("carJSP") Car car) {
        Car newCar = car;
        if (newCar!=null)
        carService.addCar(newCar);
        return "redirect:/";
    }

    @Override
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public String delCar(@RequestParam("id") long id) {
        carService.delCar(id);
        return "redirect:/";
    }

    @Override
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public ModelAndView getCars() {
        ModelAndView model = new ModelAndView("get");
        List<Car> cars = carService.getCars();
        model.addObject("cars",cars);
        return model;
    }
}
