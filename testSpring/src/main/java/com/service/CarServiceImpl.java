package com.service;

import com.DAO.CarDAO;
import com.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarServiceImpl implements CarService{

    @Autowired
    private CarDAO carDAO;

    @Transactional
    @Override
    public void addCar(Car car) {
        carDAO.addCar(car);
    }

    @Transactional
    @Override
    public void delCar(long id) {
        carDAO.delCar(id);
    }

    @Override
    public List<Car> getCars() {
        return carDAO.getCars();
    }


}
