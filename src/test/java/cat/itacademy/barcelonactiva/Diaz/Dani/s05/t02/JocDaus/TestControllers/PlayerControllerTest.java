package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.TestControllers;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.controller.PlayerController;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.ApiResponse;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        playerService = Mockito.mock(PlayerService.class);
        playerController = new PlayerController(playerService);
        authentication = Mockito.mock(Authentication.class);
    }

    @DisplayName("PlayerControllerTest - should return all players for a admin")
    @Test
    void testGetAllPlayers() {
        Date date = new Date();
        List<PlayerDTO> players = new ArrayList<>();
        players.add(new PlayerDTO(2L, "test1@mail.com", "test", Role.USER, 54, date));
        players.add(new PlayerDTO(123L, "test2@mail.com", "nick", Role.USER, 0, date));

        Collection roles = List.of(new SimpleGrantedAuthority("ADMIN"));

        when(playerService.getAllPlayersDTO()).thenReturn(players);
        when(authentication.getAuthorities()).thenReturn(roles);

        ResponseEntity<List<PlayerDTO>> responseEntity = playerController.getAllPlayers(authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(players, responseEntity.getBody());
    }

    @DisplayName("PlayerControllerTest - should not return players for a non admin roles")
    @Test
    void testGetAllPlayersWithoutAdmin() {
        Date date = new Date();
        List<PlayerDTO> players = new ArrayList<>();
        players.add(new PlayerDTO(2L, "test1@mail.com", "test", Role.USER, 54, date));
        players.add(new PlayerDTO(123L, "test@mail.com", "nick", Role.USER, 0, date));

        Collection roles = List.of(new SimpleGrantedAuthority("USER"));

        when(playerService.getAllPlayersDTO()).thenReturn(players);
        when(authentication.getAuthorities()).thenReturn(roles);

        InsufficientAuthenticationException exception = assertThrows(
                InsufficientAuthenticationException.class,
                () -> playerController.getAllPlayers(authentication)
        );

        assertEquals("You dont have permissions", exception.getMessage());
    }



    @DisplayName("PlayerControllerTest - Test getPlayerById")
    @Test
    void testGetPlayerById() {

        Long playerId = 1L;
        PlayerDTO playerDTO = new PlayerDTO(1L,"test@mail.com", "test",Role.USER, 90, new Date());
        playerDTO.setId(playerId);

        when(playerService.getPlayerByIdDTO(playerId)).thenReturn(playerDTO);

        ResponseEntity<PlayerDTO> responseEntity = playerController.getPlayerById(playerId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(playerDTO, responseEntity.getBody());
    }

    @DisplayName("PlayerControllerTest - Test updatePlayer")
    @Test
    void testUpdatePlayer() {

        Long playerId = 1L;
        Player playerDTO = new Player();
        playerDTO.setId(playerId);

        ResponseEntity<ApiResponse<PlayerDTO>> responseEntity = playerController.updatePlayer(playerId, playerDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Player updated successfully - Player ID: " + playerId, responseEntity.getBody().getMessage());
    }

    @DisplayName("PlayerControllerTest - Test deletePlayer")
    @Test
    void testDeletePlayer() {

        Long playerId = 1L;

        ResponseEntity<ApiResponse<String>> responseEntity = playerController.deletePlayer(playerId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Player deleted successfully - Player ID: " + playerId, responseEntity.getBody().getMessage());
    }
}
