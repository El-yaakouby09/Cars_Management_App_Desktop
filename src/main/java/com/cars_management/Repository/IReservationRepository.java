package com.cars_management.Repository;

import com.cars_management.Controller.Reservations.Reservation;
import java.util.List;

public interface IReservationRepository {

    boolean save(Reservation reservation);

    List<Reservation> findAll();

    boolean update(int id, Reservation r);

    boolean delete(int id);
}