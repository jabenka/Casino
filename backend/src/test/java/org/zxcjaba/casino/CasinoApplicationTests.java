package org.zxcjaba.casino;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zxcjaba.casino.api.controller.GameOneController;
import org.zxcjaba.casino.api.controller.LoginController;
import org.zxcjaba.casino.api.controller.UserController;
import org.zxcjaba.casino.api.service.AuthenticationService;
import org.zxcjaba.casino.api.service.GameResultsService;
import org.zxcjaba.casino.api.service.JwtService;
import org.zxcjaba.casino.persistence.repository.GameOneResultsRepository;
import org.zxcjaba.casino.persistence.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CasinoApplicationTests {

    @Autowired
    private GameOneController gameOneController;
    @Autowired
    private LoginController loginController;
    @Autowired
    private UserController userController;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private GameResultsService gameResultsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private GameOneResultsRepository gameOneResultsRepository;
    @Autowired
    private UserRepository UserRepository;


    @Test
    void contextLoads() {

        assertNotNull(gameOneController);
        assertNotNull(loginController);
        assertNotNull(userController);
        assertNotNull(authenticationService);
        assertNotNull(gameResultsService);
        assertNotNull(jwtService);
        assertNotNull(gameOneResultsRepository);
        assertNotNull(UserRepository);

    }
}
