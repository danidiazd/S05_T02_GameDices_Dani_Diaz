package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.controller;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.ApiResponse;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/dice")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService =  playerService;
    }

    @Operation(summary = "Get all players", description = "Retrieve a list of all players.")
    @GetMapping(path = "/getPlayers")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(Authentication authentication) {

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw  new InsufficientAuthenticationException("You dont have permissions");
        }
        List<PlayerDTO> players = playerService.getAllPlayersDTO();
        return ResponseEntity.ok().body(players);
    }

    @Operation(summary = "Get player by ID", description = "Retrieve player information by ID.")
    @GetMapping(path = "/getPlayer/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        PlayerDTO player = playerService.getPlayerByIdDTO(id);
        return ResponseEntity.ok().body(player);
    }



    @Operation(summary = "Update player", description = "Update player information.")
    @PutMapping(path = "/updatePlayer/{id}")
    public ResponseEntity<ApiResponse<PlayerDTO>> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        PlayerDTO updatedPlayer = playerService.updatePlayer(id, player);
        ApiResponse<PlayerDTO> response = new ApiResponse<>("Player updated successfully - Player ID: " + id, updatedPlayer);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete player", description = "Delete player by ID.")
    @DeleteMapping(value = "/deletePlayer/{id}")
    public ResponseEntity<ApiResponse<String>> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayerById(id);
        ApiResponse<String> response = new ApiResponse<>("Player deleted successfully - Player ID: " + id, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
