package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Controller;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Service.JwtService;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.LoginResponse;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.LoginUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.RegisterUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Service.AuthenticationService;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    @Autowired
    public JwtService jwtService;
    @Autowired
    public AuthenticationService authenticationService;



    @Operation(summary = "Registers a new player")
    @PostMapping("/signup")
    public ResponseEntity<Player> register(@RequestBody RegisterUser registerUser) {
        Player player = authenticationService.signup(registerUser);
        return ResponseEntity.ok().body(player);
    }

    @Operation(summary = "Authenticates a player and returns a JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUser loginUser) {

        Player authenticatePlayer = authenticationService.authenticate(loginUser);

        String jwtToken = jwtService.generateToken(authenticatePlayer);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok().body(loginResponse);
    }
}
