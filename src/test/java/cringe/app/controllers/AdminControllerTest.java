package cringe.app.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private AdminController controller;

    @Test
    public void contextLoads() {
        assertNotNull(controller);
    }

    @Test
    void adminPortal() {
    }

    @Test
    void newGame() {
    }

    @Test
    void editGame() {
    }

    @Test
    void testEditGame() {
    }

    @Test
    void uploadFile() {
    }

    @Test
    void allOrders() {
    }

    @Test
    void analytics() {
    }
}