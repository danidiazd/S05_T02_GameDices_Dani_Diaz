package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiceRollDTO {
    private Long playerId;
    private int dice1;
    private int dice2;
    private int diceResult;
    private boolean win;
    private Date playedOnDate;
    private double successRate;
}
