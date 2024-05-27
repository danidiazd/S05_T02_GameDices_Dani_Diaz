package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Service;


import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.LoginUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth.RegisterUser;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.BadCredentialsException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    public Player signup(RegisterUser input) {

        if (playerRepository.findUserByEmailIgnoreCase(input.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (playerRepository.findUserByNickIgnoreCase(input.getNick()).isPresent()) {
            throw new RuntimeException("Nick already exists");
        }

        Player player = new Player();
        player.setNick(input.getNick());
        player.setEmail(input.getEmail());

        String encodedPassword = passwordEncoder.encode(input.getPassword());
        player.setPassword(encodedPassword);

        if (input.getNick().isBlank()) {
            player.setNick("ANONIMOUS");
            player.setRole(Role.NO_ROLE);
        }
        if (input.getNick().startsWith("admin")) {
            player.setRole(Role.ADMIN);
        }
        return playerRepository.save(player);
    }

    public Player authenticate(LoginUser input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Email or password is incorrect");
        }
        return playerRepository.findUserByEmailIgnoreCase(input.getEmail())
                .orElseThrow(() -> new PlayerNotFoundException("Player not found for email: " + input.getEmail()));
    }
}
