package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;

import java.util.List;

public interface PlayerService {

    List<PlayerDTO> getAllPlayersDTO();

    PlayerDTO getPlayerByIdDTO(Long id);


    PlayerDTO updatePlayer(Long id, Player updatePlayer);

    void deletePlayerById(Long id) throws PlayerNotFoundException;

}
