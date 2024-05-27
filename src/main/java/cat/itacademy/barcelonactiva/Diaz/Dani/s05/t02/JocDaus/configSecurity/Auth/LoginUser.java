package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.configSecurity.Auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginUser {

    private String email;
    private String password;
}
