package com.cars_management.Controller.Reservations;

public class ReservationService {

    private static final double PRIX_JOUR = 200.0;

    public double calculerTotal(int jours) {
        return jours * PRIX_JOUR;
    }

}
