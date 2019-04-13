package com.DAO;

import com.model.Car;

import java.util.List;

public interface CarDAO {
    public void addCar(Car car);
    public void delCar(long id);
    public Car getById(long id);
    public List<Car> getCars();
}
