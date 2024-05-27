package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.ServiceTest;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.GameNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.DiceRoll;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.DiceRollDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.DiceRollRepository;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services.DiceRollServiceMongoDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DiceRollServiceMongoDBTest {

    @Mock
    private DiceRollRepository diceRollRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private DiceRollServiceMongoDB diceRollService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test new game - Player not found")
    @Test
    void testNewGame_PlayerNotFound() {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> {
            diceRollService.newGame(playerId);
        });
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test new game - Success")
    @Test
    void testNewGame_Success() {
        Long playerId = 1L;
        Player player = Player.builder().id(playerId).email("test@example.com").password("password").nick("testnick").role(Role.USER).winRate(0).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(diceRollRepository.save(any(DiceRoll.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DiceRollDTO newGame = diceRollService.newGame(playerId);

        assertNotNull(newGame);
        assertEquals(playerId, newGame.getPlayerId());
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test get game by ID - Game not found")
    @Test
    void testGetGameByIdDTO_GameNotFound() {
        String gameId = "gameId";

        when(diceRollRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> {
            diceRollService.getGameByIdDTO(gameId);
        });
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test get game by ID - Success")
    @Test
    void testGetGameByIdDTO_Success() {
        String gameId = "gameId";
        DiceRoll diceRoll = DiceRoll.builder().id(gameId).player(1L).dice1(3).dice2(4).wins(true).playedOnDate(new Date()).build();

        when(diceRollRepository.findById(gameId)).thenReturn(Optional.of(diceRoll));

        DiceRollDTO diceRollDTO = diceRollService.getGameByIdDTO(gameId);

        assertNotNull(diceRollDTO);
        assertEquals(1L, diceRollDTO.getPlayerId());
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test get all games")
    @Test
    void testGetAllGamesDTO() {
        List<DiceRoll> diceRolls = new ArrayList<>();
        diceRolls.add(DiceRoll.builder().id("1").player(1L).dice1(3).dice2(4).wins(true).playedOnDate(new Date()).build());
        diceRolls.add(DiceRoll.builder().id("2").player(2L).dice1(1).dice2(5).wins(false).playedOnDate(new Date()).build());

        when(diceRollRepository.findAllByOrderByPlayedOnDate()).thenReturn(diceRolls);

        List<DiceRollDTO> diceRollDTOs = diceRollService.getAllGamesDTO();

        assertEquals(diceRolls.size(), diceRollDTOs.size());
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test get all player games by player ID")
    @Test
    void testGetAllPlayerGamesDTO() {
        Long playerId = 1L;
        List<DiceRoll> diceRolls = new ArrayList<>();
        diceRolls.add(DiceRoll.builder().id("1").player(playerId).dice1(3).dice2(4).wins(true).playedOnDate(new Date()).build());
        diceRolls.add(DiceRoll.builder().id("2").player(playerId).dice1(1).dice2(5).wins(false).playedOnDate(new Date()).build());

        when(diceRollRepository.findAllByPlayer(playerId)).thenReturn(diceRolls);

        List<DiceRollDTO> diceRollDTOs = diceRollService.getAllPlayerGamesDTO(playerId);

        assertEquals(diceRolls.size(), diceRollDTOs.size());
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test delete games by player - Games not found")
    @Test
    void testDeleteGamesByPlayer_GamesNotFound() {
        Long playerId = 1L;

        when(diceRollRepository.findAllByPlayer(playerId)).thenReturn(new ArrayList<>());

        assertThrows(GameNotFoundException.class, () -> {
            diceRollService.deleteGamesByPlayer(playerId);
        });
    }

    @DisplayName("DiceRollServiceMongoDBTest - Test delete games by player - Success")
    @Test
    void testDeleteGamesByPlayer_Success() {
        Long playerId = 1L;
        List<DiceRoll> diceRolls = new ArrayList<>();
        diceRolls.add(DiceRoll.builder().id("1").player(playerId).dice1(3).dice2(4).wins(true).playedOnDate(new Date()).build());

        when(diceRollRepository.findAllByPlayer(playerId)).thenReturn(diceRolls);
        doNothing().when(diceRollRepository).deleteAllByPlayer(playerId);

        diceRollService.deleteGamesByPlayer(playerId);

        verify(diceRollRepository, times(1)).deleteAllByPlayer(playerId);
    }
}
