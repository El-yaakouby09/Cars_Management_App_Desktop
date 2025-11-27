package com.cars_management.Repository;

import com.cars_management.Controller.Cars.Car;
import java.util.List;

public interface ICarRepository {

    boolean save(Car car);

    boolean update(Car car);

    boolean delete(int id);

    List<Car> findAll();
}

