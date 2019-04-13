package com.DAO;

import com.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CarDAOImpl implements CarDAO {
    private static final Logger logger = LoggerFactory.getLogger(CarDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override   
    public void addCar(Car car) {
            Car newCar = new Car();
            newCar.setFirm(car.getFirm());
            newCar.setModel(car.getModel());
            newCar.setColor(car.getColor());
            newCar.setPower(car.getPower());
            entityManager.persist(newCar);
            logger.info("Car successfully added. Car info:" + newCar);
    }

    @Override
    public void delCar(long id) {
        Car oldCar = entityManager.find(Car.class,id);
        if (oldCar!=null){
            entityManager.remove(oldCar);
            logger.info("Car successfully deleted. Car info:" + oldCar);
        }
    }

    @Override
    public Car getById(long id) {
        logger.info("Car successfully loaded. Car info: ID=" + id);
        return entityManager.find(Car.class,id);
    }

    @Override
    public List<Car> getCars() {
        logger.info("All cars successfully loaded");
        return entityManager.createQuery("from Car",Car.class).getResultList();
    }
}
