package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.TestControllers;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.controller.DiceRollController;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.ApiResponse;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.DiceRollDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services.DiceRollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class DiceRollControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Mock
    private DiceRollService diceRollService;

    @InjectMocks
    private DiceRollController diceRollController;

    @BeforeEach
    void setUp() {
        diceRollService = Mockito.mock(DiceRollService.class);
        diceRollController = new DiceRollController(diceRollService);
        List<DiceRollDTO> expectedGames = List.of(
                new DiceRollDTO(1L, 1, 2, 3, true, new Date(), 50.0),
                new DiceRollDTO(2L, 4, 5, 9, false, new Date(), 25.0)
        );
        when(diceRollService.getAllGamesDTO()).thenReturn(expectedGames);
    }


    @DisplayName("GameControllerTest - Test getAllGames")
    @Test
    void testGetAllGames() {

        List<DiceRollDTO> games = new ArrayList<>();
        games.add(new DiceRollDTO());
        games.add(new DiceRollDTO(123L, 1, 2, 3, true, new Date(), 50.0));
        when(diceRollService.getAllGamesDTO()).thenReturn(games);

        ResponseEntity<List<DiceRollDTO>> responseEntity = diceRollController.getAllGames();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(games, responseEntity.getBody());
    }

    @DisplayName("GameControllerTest - Test getAllGamesByPlayer")
    @Test
    void testGetGamesByPlayer() {
        List<DiceRollDTO> playerGames = new ArrayList<>();
        playerGames.add(new DiceRollDTO());
        playerGames.add(new DiceRollDTO(2L, 1, 2, 3, true, new Date(), 50.0));

        when(diceRollService.getAllPlayerGamesDTO(2L)).thenReturn(playerGames);

        ResponseEntity<List<DiceRollDTO>> responseEntity = diceRollController.getAllGamesByPlayer(2L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(playerGames, responseEntity.getBody());
    }

    @DisplayName("GameControllerTest - Test getBetterWinRatePlayer")
    @Test
    void testGetBetterWinRatePlayer() {

        PlayerDTO player1 = new PlayerDTO(1L, "test@test.com", "test1", Role.USER, 92.0, new Date());
        PlayerDTO player2 = new PlayerDTO(2L, "test2@test2.com", "test2", Role.USER, 65.0, new Date());

        when(diceRollService.getBetterWinRate()).thenReturn(player1);

        ResponseEntity<PlayerDTO> responseEntity = diceRollController.getBetterWinRatePlayer();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(player1, responseEntity.getBody());
        assertThat(responseEntity.getBody().getWinRate(), is(92.0));
    }

    @DisplayName("GameControllerTest - Test getWorstWinRatePlayer")
    @Test
    void testGetWorstWinRatePlayer() {

        PlayerDTO player1 = new PlayerDTO(1L, "test@test.com", "test1", Role.USER, 92.0, new Date());
        PlayerDTO player2 = new PlayerDTO(2L, "test2@test2.com", "test2", Role.USER, 65.0, new Date());

        when(diceRollService.getWorstWinRate()).thenReturn(player2);

        ResponseEntity<PlayerDTO> responseEntity = diceRollController.getWorstWinRatePlayer();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(player2, responseEntity.getBody());
        assertThat(responseEntity.getBody().getWinRate(), is(65.0));
    }

    @DisplayName("GameControllerTest - Test saveGame")
    @Test
    void testSaveGame() {

        DiceRollDTO diceRoll = new DiceRollDTO(1L, 3, 4, 7, true, new Date(), 92.0);
        Mockito.when(diceRollService.newGame(1L)).thenReturn(diceRoll);
        ResponseEntity<ApiResponse<DiceRollDTO>> responseEntity = diceRollController.saveGame(1L);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getData().getPlayerId());
        assertEquals("Game created successfully", responseEntity.getBody().getMessage());
        assertTrue(responseEntity.getBody().getData().isWin());
        assertThat(responseEntity.getBody().getData().getSuccessRate(), is(92.0));


    }


    @DisplayName("GameControllerTest - Test deleteGame")
    @Test
    void testDeleteGame() {

        ResponseEntity<ApiResponse<String>> responseEntity = diceRollController.deleteGame(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Game deleted successfully - Game ID: 1", responseEntity.getBody().getMessage());
    }
}
