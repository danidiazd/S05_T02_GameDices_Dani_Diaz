package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponse {

    private String token;

    private long expiresIn;
}
