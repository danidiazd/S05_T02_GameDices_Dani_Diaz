package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDTO {

    private Long id;
    private String email;
    private String nick;
    private Role role;
    private double winRate;
    private Date registrationDate;

}
