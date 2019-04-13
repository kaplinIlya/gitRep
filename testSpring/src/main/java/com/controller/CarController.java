package com.controller;

import com.model.Car;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface CarController {
    public String add(ModelMap model);
    public String delCar(long id);
    public ModelAndView getCars();
}
