package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.TestControllers;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Controller.AuthenticationController;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Service.JwtService;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.LoginResponse;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.LoginUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.RegisterUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Service.AuthenticationService;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        jwtService = Mockito.mock(JwtService.class);
        authenticationService = Mockito.mock(AuthenticationService.class);
        authenticationController = new AuthenticationController();
        authenticationController.jwtService = jwtService;
        authenticationController.authenticationService = authenticationService;
    }

    @DisplayName("AuthenticationControllerTest - Test register - Success")
    @Test
    void testRegister() {
        RegisterUser registerUser = new RegisterUser();
        Player player = new Player();
        when(authenticationService.signup(registerUser)).thenReturn(player);

        ResponseEntity<Player> responseEntity = authenticationController.register(registerUser);

        assertEquals(player, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @DisplayName("AuthenticationControllerTest - Test authenticate - Success")
    @Test
    void testAuthenticate() {
        LoginUser loginUser = new LoginUser();
        Player player = new Player();
        String token = "jwtToken";
        when(authenticationService.authenticate(loginUser)).thenReturn(player);
        when(jwtService.generateToken(player)).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        ResponseEntity<LoginResponse> responseEntity = authenticationController.authenticate(loginUser);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(token, responseEntity.getBody().getToken());
        assertEquals(3600L, responseEntity.getBody().getExpiresIn());
    }
}
