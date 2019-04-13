package com.service;

import com.model.Car;

import java.util.List;

public interface CarServiceREST {
    public Car getByID(long id);
    public List<Car> getAll();
    public void add(Car car);
    public void delete(long id);
}
