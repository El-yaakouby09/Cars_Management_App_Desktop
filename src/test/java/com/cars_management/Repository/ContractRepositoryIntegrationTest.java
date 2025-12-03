package com.cars_management.Repository;

import com.cars_management.Controller.Contracts.Contract;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractRepositoryIntegrationTest {

    private static ContractRepository repo;

    @BeforeAll
    static void setup() {
        // repository uses your Docker DB automatically
        repo = new ContractRepository();
    }

    private Contract buildSampleContract() {
        return new Contract(
                null,
                "HAMZA",
                "CNI123",
                "0667000000",
                "Casa",
                LocalDate.of(2020, 5, 5),
                "BMW",
                "X5",
                "1234-A-10",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 5),
                4,
                300.0,
                1200.0
        );
    }

    @Test
    @Order(1)
    void testInsertContract() {
        int before = repo.countContracts();

        Contract c = buildSampleContract();
        repo.save(c);

        int after = repo.countContracts();

        Assertions.assertEquals(before + 1, after);
    }

    @Test
    @Order(2)
    void testFindAll() {
        List<Contract> list = repo.findAll();

        Assertions.assertTrue(list.size() > 0);
        Assertions.assertEquals("HAMZA", list.get(0).getClientName());
    }

    @Test
    @Order(3)
    void testUpdateContract() {
        List<Contract> list = repo.findAll();
        Contract c = list.get(0);
        c.setClientName("HAMZA UPDATED");

        repo.update(c);

        List<Contract> updatedList = repo.findAll();

        Assertions.assertEquals("HAMZA UPDATED", updatedList.get(0).getClientName());
    }

    @Test
    @Order(4)
    void testDeleteContract() {
        List<Contract> list = repo.findAll();
        int idToDelete = list.get(0).getId();

        int before = repo.countContracts();
        repo.delete(idToDelete);
        int after = repo.countContracts();

        Assertions.assertEquals(before - 1, after);
    }
}
