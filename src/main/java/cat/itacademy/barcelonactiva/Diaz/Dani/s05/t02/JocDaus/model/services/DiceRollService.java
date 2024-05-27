package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.GameNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.DiceRollDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;

import java.util.List;

public interface DiceRollService {


    List<DiceRollDTO> getAllGamesDTO();

    List<PlayerDTO> getAllPlayersSortedByWinRate();

    PlayerDTO getBetterWinRate();

    PlayerDTO getWorstWinRate();

    List<DiceRollDTO> getAllPlayerGamesDTO(Long playerId);

    DiceRollDTO getGameByIdDTO(String id);
    DiceRollDTO newGame(Long id);

    void deleteGamesByPlayer(Long id) throws GameNotFoundException;
}
