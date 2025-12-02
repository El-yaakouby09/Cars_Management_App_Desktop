package com.cars_management.Repository;

import com.cars_management.TopCar;

public interface ITopCarRepository {
    TopCar findTopCar();
    void refresh();
}