package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.LoginUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.RegisterUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Service.AuthenticationService;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;

import java.util.Optional;

@SpringBootTest
public class AuthenticationServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("AuthenticationServiceTest - Test signup - Email already exists")
    @Test
    void testSignup() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("test@example.com");
        registerUser.setNick("testnick");
        registerUser.setPassword("password");

        Player existingPlayerByEmail = new Player();
        existingPlayerByEmail.setEmail("test@example.com");

        Player existingPlayerByNick = new Player();
        existingPlayerByNick.setNick("testnick");

        when(playerRepository.findUserByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(existingPlayerByEmail));
        when(playerRepository.findUserByNickIgnoreCase("testnick")).thenReturn(Optional.of(existingPlayerByNick));

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.signup(registerUser);
        });

        assertEquals("Email already exists", exception.getMessage());
    }

    @DisplayName("AuthenticationServiceTest - Test authenticate - Success")
    @Test
    void testAuthenticate() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail("test@test.com");
        loginUser.setPassword("password");

        Player existingPlayer = new Player();
        existingPlayer.setEmail("test@test.com");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(playerRepository.findUserByEmailIgnoreCase("test@test.com")).thenReturn(Optional.of(existingPlayer));

        Player authenticatedPlayer = authenticationService.authenticate(loginUser);
        assertNotNull(authenticatedPlayer);
        assertEquals("test@test.com", authenticatedPlayer.getEmail());
    }

    @DisplayName("AuthenticationServiceTest - Test authenticate - User not found")
    @Test
    void testAuthenticate_UserNotFound() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail("nonexistent@test.com");
        loginUser.setPassword("password");

        when(playerRepository.findUserByEmailIgnoreCase("nonexistent@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(loginUser);
        });
    }
}
