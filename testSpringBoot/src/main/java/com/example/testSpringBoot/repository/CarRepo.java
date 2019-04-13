package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepo extends JpaRepository<Car, Long> {
    public List<Car> findByModel(String model);

    @Query("select c from Car c where c.firm = :firm")
    public List<Car> findCarByFirm(@Param("firm")String firm);
}