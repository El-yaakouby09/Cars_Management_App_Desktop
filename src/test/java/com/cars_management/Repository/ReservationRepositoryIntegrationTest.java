package com.cars_management.Repository;

import com.cars_management.Controller.Reservations.Reservation;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationRepositoryIntegrationTest {

    private static ReservationRepository repo;

    @BeforeAll
    static void setup() {
        repo = new ReservationRepository();
    }

    private Reservation buildSampleReservation() {
        return new Reservation(
                null,
                "HAMZA",
                "BMW X5 - 1234",
                3,
                900.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 4),
                300.0
        );
    }

    @Test
    @Order(1)
    void testSaveReservation() {
        Reservation r = buildSampleReservation();
        boolean ok = repo.save(r);

        Assertions.assertTrue(ok, "La réservation doit être enregistrée !");
    }

    @Test
    @Order(2)
    void testFindAll() {
        List<Reservation> list = repo.findAll();

        Assertions.assertFalse(list.isEmpty(), "La liste des réservations ne doit pas être vide !");
        Assertions.assertEquals("HAMZA", list.get(0).getClient());
    }

    @Test
    @Order(3)
    void testUpdateReservation() {
        List<Reservation> list = repo.findAll();
        Reservation r = list.get(0);

        Reservation updated = new Reservation(
                r.getId(),
                "HAMZA UPDATED",
                r.getCar(),
                r.getDays(),
                r.getTotal(),
                r.getStartDate(),
                r.getEndDate(),
                r.getPricePerDay()
        );

        boolean ok = repo.update(r.getId(), updated);

        Assertions.assertTrue(ok, "La mise à jour doit réussir !");

        List<Reservation> list2 = repo.findAll();
        Assertions.assertEquals("HAMZA UPDATED", list2.get(0).getClient());
    }

    @Test
    @Order(4)
    void testDeleteReservation() {
        List<Reservation> list = repo.findAll();
        int idToDelete = list.get(0).getId();

        boolean ok = repo.delete(idToDelete);

        Assertions.assertTrue(ok, "La suppression doit réussir !");

        List<Reservation> list2 = repo.findAll();
        Assertions.assertTrue(list2.stream().noneMatch(r -> r.getId() == idToDelete));
    }
}
