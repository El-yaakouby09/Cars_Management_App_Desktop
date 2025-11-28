package com.cars_management.Controller.Reservations;

import com.cars_management.Repository.IReservationRepository;
import com.cars_management.Repository.ReservationRepository;

import java.util.List;

public class ReservationService {

    private final IReservationRepository repo;

    private static final double PRIX_JOUR = 200.0;

    public ReservationService() {
        this.repo = new ReservationRepository();
    }

    public double calculerTotal(int jours) {
        return jours * PRIX_JOUR;
    }

    public boolean save(Reservation r) {
        return repo.save(r);
    }

    public List<Reservation> findAll() {
        return repo.findAll();
    }

    public boolean update(int id, Reservation r) {
        return repo.update(id, r);
    }

    public boolean delete(int id) {
        return repo.delete(id);
    }
}
