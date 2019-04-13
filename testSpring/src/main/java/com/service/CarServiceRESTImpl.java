package com.service;

import com.DAO.CarDAO;
import com.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "carServiceREST")
public class CarServiceRESTImpl implements CarServiceREST {

    @Autowired
    private CarDAO carDAO;

    @Override
    public Car getByID(long id) {
        return carDAO.getById(id);
    }

    @Override
    public List<Car> getAll() {

        return carDAO.getCars();
    }

    @Transactional
    @Override
    public void add(Car car) {

        carDAO.addCar(car);
    }

    @Transactional
    @Override
    public void delete(long id) {
        carDAO.delCar(id);

    }
}
