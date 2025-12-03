package com.cars_management.Repository;

import com.cars_management.TopCar;
import com.cars_management.Controller.Contracts.Contract;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TopCarRepositoryIntegrationTest {

    private static ContractRepository contractRepo;
    private static TopCarRepository topCarRepo;

    @BeforeAll
    static void setup() {
        contractRepo = new ContractRepository();
        topCarRepo = new TopCarRepository();
    }

    private Contract buildContract(String marque, String model, String plate) {
        return new Contract(
                null,
                "Client Test",
                "CNI123",
                "0661000000",
                "City",
                LocalDate.of(2019, 5, 5),
                marque,
                model,
                plate,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2),
                1,
                200.0,
                200.0
        );
    }

    @Test
    @Order(1)
    void testInsertDataForTopCar() {
        // BMW louée 3 fois
        contractRepo.save(buildContract("BMW", "X5", "B-111"));
        contractRepo.save(buildContract("BMW", "X5", "B-111"));
        contractRepo.save(buildContract("BMW", "X5", "B-111"));

        // Audi louée 1 fois
        contractRepo.save(buildContract("Audi", "A4", "A-222"));
    }

    @Test
    @Order(2)
    void testRefreshMView() {
        Assertions.assertDoesNotThrow(() -> {
            topCarRepo.refresh();
        });
    }

    @Test
    @Order(3)
    void testFindTopCar() {
        TopCar top = topCarRepo.findTopCar();

        Assertions.assertNotNull(top, "La vue ne doit pas être vide !");
        Assertions.assertEquals("BMW", top.getMarque());
        Assertions.assertEquals("X5", top.getModel());
    }
}
