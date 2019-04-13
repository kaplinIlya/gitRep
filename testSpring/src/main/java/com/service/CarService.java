package com.service;

import com.model.Car;

import java.util.List;

public interface CarService {
    public void addCar(Car car);
    public void delCar(long id);
    public List<Car> getCars();
}
