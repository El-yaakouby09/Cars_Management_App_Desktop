package com.cars_management.Repository;

import com.cars_management.Controller.Cars.Car;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarRepositoryTest {

    private static CarRepository repo;

    @BeforeAll
    static void setup() {
        repo = new CarRepository();
    }

    @BeforeEach
    void cleanDatabase() {
        try (Connection c = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/cars_db",
                "postgres",
                "postgres")) {

            c.createStatement().execute("DELETE FROM cars");

        } catch (SQLException e) {
            fail("❌ Could not clean database: " + e.getMessage());
        }
    }

    // ----------------------------
    // 1. TEST SAVE
    // ----------------------------
    @Test
    @Order(1)
    void testSaveCar() {

        Car car = new Car(1, "BMW", "320D", 2020, 350.0);
        car.setMatricule("1234-A");

        boolean result = repo.save(car);

        assertTrue(result, "Car should be saved successfully");
        assertEquals(1, repo.countCars(), "Cars count should be 1");
    }

    // ----------------------------
    // 2. TEST FIND ALL
    // ----------------------------
    @Test
    @Order(2)
    void testFindAll() {

        Car car = new Car(1, "BMW", "320D", 2020, 350.0);
        car.setMatricule("1234-A");
        repo.save(car);

        var cars = repo.findAll();

        assertEquals(1, cars.size(), "Should return 1 car");
        assertEquals("1234-A", cars.get(0).getMatricule());
        assertEquals("BMW 320D", cars.get(0).getMarqueModel());
    }

    // ----------------------------
    // 3. TEST UPDATE
    // ----------------------------
    @Test
    @Order(3)
    void testUpdateCar() {

        Car car = new Car(1, "BMW", "320D", 2020, 350);
        car.setMatricule("1234-A");
        repo.save(car);

        Car updated = new Car(1, "Audi", "A4", 2022, 500);
        updated.setMatricule("9999-Z");

        boolean result = repo.update(updated);
        assertTrue(result, "Update should succeed");

        var cars = repo.findAll();
        assertEquals("9999-Z", cars.get(0).getMatricule());
        assertEquals("Audi A4", cars.get(0).getMarqueModel());
    }

    // ----------------------------
    // 4. TEST DELETE
    // ----------------------------
    @Test
    @Order(4)
    void testDeleteCar() {

        Car car = new Car(1, "BMW", "320D", 2020, 350);
        car.setMatricule("1234-A");
        repo.save(car);

        boolean result = repo.delete(1);
        assertTrue(result, "Delete should succeed");

        assertEquals(0, repo.countCars(), "Cars count should be 0 after delete");
    }
}
