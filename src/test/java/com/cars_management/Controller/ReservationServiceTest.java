package com.cars_management.Controller.Reservations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReservationServiceTest {

    private final ReservationService service = new ReservationService();

    @Test
    void testCalculTotal() {
        double total = service.calculerTotal(5, 200);
        Assertions.assertEquals(1000, total);
    }

    @Test
    void testCalculTotalZeroDays() {
        double total = service.calculerTotal(0, 300);
        Assertions.assertEquals(0, total);
    }

    @Test
    void testCalculTotalNegativeDays() {
        double total = service.calculerTotal(-2, 300);
        Assertions.assertEquals(-600, total);
    }

    @Test
    void testCalculTotalDecimalPrice() {
        double total = service.calculerTotal(3, 199.99);
        Assertions.assertEquals(599.97, total, 0.001);
    }
}
