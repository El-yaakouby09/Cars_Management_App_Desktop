package com.cars_management.Repository;

import com.cars_management.Controller.Auth.User;
import org.junit.jupiter.api.*;

import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryIntegrationTest {

    private static UserRepository userRepo;

    @BeforeAll
    static void setup() {
        userRepo = new UserRepository();
    }

    @Test
    @Order(1)
    void testDefaultAdminUserExists() {
        User admin = userRepo.findByUsername("admin");

        Assertions.assertNotNull(admin, "L’utilisateur admin doit exister !");
        Assertions.assertEquals("admin", admin.getUsername());
        Assertions.assertEquals("admin", admin.getPassword());
    }

    @Test
    @Order(2)
    void testSaveUser() {
        User user = new User(null, "hamza", "1234");
        userRepo.save(user);

        User saved = userRepo.findByUsername("hamza");

        Assertions.assertNotNull(saved, "L’utilisateur hamza doit être enregistré !");
        Assertions.assertEquals("hamza", saved.getUsername());
        Assertions.assertEquals("1234", saved.getPassword());
    }

    @Test
    @Order(3)
    void testUpdatePassword() {
        boolean updated = userRepo.updatePassword("hamza", "9999");

        Assertions.assertTrue(updated, "Le mot de passe doit être mis à jour !");

        User updatedUser = userRepo.findByUsername("hamza");

        Assertions.assertEquals("9999", updatedUser.getPassword(),
                "Le mot de passe mis à jour doit être enregistré en BD !");
    }

    @Test
    @Order(4)
    void testUpdateUsername() {
        boolean updated = userRepo.updateUsername("hamza", "hamza_new");

        Assertions.assertTrue(updated, "Le username doit être mis à jour !");

        User updatedUser = userRepo.findByUsername("hamza_new");

        Assertions.assertNotNull(updatedUser, "Le nouvel username doit exister !");
        Assertions.assertEquals("hamza_new", updatedUser.getUsername());
    }

    @Test
    @Order(5)
    void testLoginFailsWrongUser() {
        User user = userRepo.findByUsername("not_existing_user");

        Assertions.assertNull(user, "Chercher un user qui n'existe pas doit retourner null");
    }

    @Test
    @Order(6)
    void testLoginFailsWrongPassword() {
        User admin = userRepo.findByUsername("admin");

        Assertions.assertNotNull(admin);

        Assertions.assertFalse(
                Objects.equals(admin.getPassword(), "wrong"),
                "Le mot de passe ne doit pas correspondre !"
        );
    }

}
