package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.controller;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.GameNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.ApiResponse;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.DiceRollDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services.DiceRollService;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services.PlayerServiceSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/diceGame")
public class DiceRollController {

    @Autowired
    private DiceRollService diceRollService;
    @Autowired
    private PlayerServiceSQL playerServiceSQL;

    public DiceRollController(DiceRollService diceRollService) {
        this.diceRollService = diceRollService;
    }

    @Operation(summary = "Get all games", description = "Retrieve a list of all games.")
    @GetMapping(path = "/getGames")
    public ResponseEntity<List<DiceRollDTO>> getAllGames() {
        List<DiceRollDTO> games = diceRollService.getAllGamesDTO();
        return ResponseEntity.ok().body(games);
    }

    @Operation(summary = "Get games by player", description = "Retrieve a list of games played by a specific player.")
    @GetMapping(path = "/getGamesByPlayer/{playerId}")
    public ResponseEntity<List<DiceRollDTO>> getAllGamesByPlayer(@PathVariable Long playerId) {
        List<DiceRollDTO> games = diceRollService.getAllPlayerGamesDTO(playerId);
        return ResponseEntity.ok().body(games);
    }

    @Operation(summary = "Get players by win rate", description = "Retrieve a list of players sorted by win rate.")
    @GetMapping(path = "/getPlayersByWinRate")
    public ResponseEntity<List<PlayerDTO>> getPlayersByWinRate() {
        List<PlayerDTO> players = diceRollService.getAllPlayersSortedByWinRate();
        return ResponseEntity.ok().body(players);
    }

    @Operation(summary = "Get player with better win rate", description = "Retrieve the player with the best win rate.")
    @GetMapping(path = "/getBetterWinRatePlayer")
    public ResponseEntity<PlayerDTO> getBetterWinRatePlayer() {
        PlayerDTO player = diceRollService.getBetterWinRate();
        return ResponseEntity.ok().body(player);
    }

    @Operation(summary = "Get player with worst win rate", description = "Retrieve the player with the worst win rate.")
    @GetMapping(path = "/getWorstWinRatePlayer")
    public ResponseEntity<PlayerDTO> getWorstWinRatePlayer() {
        PlayerDTO player = diceRollService.getWorstWinRate();
        return ResponseEntity.ok().body(player);
    }

    @Operation(summary = "Create a new game", description = "Create a new game for a specific player.")
    @PostMapping(path = "/newGame/{id}")
    public ResponseEntity<ApiResponse<DiceRollDTO>> saveGame(@PathVariable Long id) {
        DiceRollDTO newGame = diceRollService.newGame(id);
        ApiResponse<DiceRollDTO> response = new ApiResponse<>("Game created successfully", newGame);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete game", description = "Delete all games played by a specific player.")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGame(@PathVariable Long id) {
        try {
            diceRollService.deleteGamesByPlayer(id);
            ApiResponse<String> response = new ApiResponse<>("Game deleted successfully - Game ID: " + id, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (GameNotFoundException e) {
            ApiResponse<String> response = new ApiResponse<>("Game not found - Game ID: " + id, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
