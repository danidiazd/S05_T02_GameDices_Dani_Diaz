package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "games")
public class DiceRoll {


    private String id;
    private Long player; //ID del jugador en SQL
    private int dice1;
    private int dice2;
    private boolean wins;
    private Date playedOnDate;

}
