package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.PlayerAlreadyExistException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.DiceRoll;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.DiceRollRepository;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerServiceSQL implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private DiceRollRepository diceRollRepository;

    @Override
    public List<PlayerDTO> getAllPlayersDTO() {
        List<Player> playerList = playerRepository.findAllByOrderById();
        return playerList.stream().map(this::convertToDTO).toList();
    }

    @Override
    public PlayerDTO getPlayerByIdDTO(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player Not Found with ID: " + id));
        return convertToDTO(player);
    }


    @Override
    public PlayerDTO updatePlayer(Long id, Player updatedPlayer) {

        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player Not Found with ID " + id));

        existingPlayer.setNick(updatedPlayer.getNick());
        Player savedPlayer = playerRepository.save(existingPlayer);
        return convertToDTO(savedPlayer);
    }

    @Override
    public void deletePlayerById(Long id) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player Not Found with ID: " + id));
        playerRepository.deleteById(existingPlayer.getId());
    }



    private PlayerDTO convertToDTO(Player player) {
        return new PlayerDTO(player.getId(), player.getEmail(), player.getNick(), player.getRole(), player.getWinRate(), player.getRegistrationDate());
    }


}
