package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.GameNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.DiceRoll;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.DiceRollDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.DiceRollRepository;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiceRollServiceMongoDB implements DiceRollService {

    @Autowired
    private DiceRollRepository diceRollRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public DiceRollDTO newGame(Long playerId) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        if (playerOptional.isEmpty()) {
            throw new PlayerNotFoundException("Player Not Found with ID: " + playerId);
        }

        Player player = playerOptional.get();
        DiceRoll newDiceRoll = new DiceRoll();
        newDiceRoll.setPlayer(playerId);
        int dice1 = rollDice();
        int dice2 = rollDice();
        Date playedDate = new Date();
        newDiceRoll.setDice1(dice1);
        newDiceRoll.setDice2(dice2);
        newDiceRoll.setWins(dice1 + dice2 == 7);
        newDiceRoll.setPlayedOnDate(playedDate);

        diceRollRepository.save(newDiceRoll);


        double winRate = calculateSuccessRate(playerId);
        player.setWinRate(winRate);
        playerRepository.save(player);

        return convertToDTO(newDiceRoll);
    }

    @Override
    public DiceRollDTO getGameByIdDTO(String id) {
        DiceRoll diceRoll = diceRollRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game Not Found with ID: " + id));
        return convertToDTO(diceRoll);
    }

    @Override
    public List<DiceRollDTO> getAllGamesDTO() {
        List<DiceRoll> diceRollList = diceRollRepository.findAllByOrderByPlayedOnDate();
        return diceRollList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerDTO> getAllPlayersSortedByWinRate() {
        List<Player> players = playerRepository.findAllByOrderByWinRateDesc();
        return players.stream()
                .map(this::convertToPlayerDTO)
                .collect(Collectors.toList());
    }


    @Override
    public PlayerDTO getBetterWinRate() {

        Player player = playerRepository.findTopByOrderByWinRateDesc();
        if(player == null) throw new PlayerNotFoundException("No players found");
        return convertToPlayerDTO(player);
    }

    @Override
    public PlayerDTO getWorstWinRate() {
        Player player = playerRepository.findTopByOrderByWinRateAsc();

        if(player == null) throw new PlayerNotFoundException("No players found");
        return convertToPlayerDTO(player);
    }

    @Override
    public List<DiceRollDTO> getAllPlayerGamesDTO(Long playerId) {
        List<DiceRoll> playerGames = diceRollRepository.findAllByPlayer(playerId);
        return playerGames.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGamesByPlayer(Long playerId) throws GameNotFoundException {
        List<DiceRoll> existingGames = diceRollRepository.findAllByPlayer(playerId);
        if (existingGames.isEmpty()) {
            throw new GameNotFoundException("No games found for player with ID: " + playerId);
        }
        diceRollRepository.deleteAllByPlayer(playerId);
    }


    private DiceRollDTO convertToDTO(DiceRoll diceRoll) {
        int diceResult = diceRoll.getDice1() + diceRoll.getDice2();
        boolean win = diceRoll.isWins();
        double successRate = calculateSuccessRate(diceRoll.getPlayer());



        return new DiceRollDTO(
                diceRoll.getPlayer(),
                diceRoll.getDice1(),
                diceRoll.getDice2(),
                diceResult,
                win,
                diceRoll.getPlayedOnDate(),
                successRate
        );
    }

    private double calculateSuccessRate(Long playerId) {
        List<DiceRoll> playerGames = diceRollRepository.findAllByPlayer(playerId);
        if (playerGames.isEmpty()) {
            return 0;
        }

        long wins = playerGames.stream().filter(DiceRoll::isWins).count();
        double successRate = (double) wins / playerGames.size() * 100;

        BigDecimal bd = new BigDecimal(successRate).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }

    private PlayerDTO convertToPlayerDTO(Player player) {
        double winRate = calculateSuccessRate(player.getId());
        return new PlayerDTO(
                player.getId(),
                player.getEmail(),
                player.getNick(),
                player.getRole(),
                winRate,
                player.getRegistrationDate()
        );
    }
}
