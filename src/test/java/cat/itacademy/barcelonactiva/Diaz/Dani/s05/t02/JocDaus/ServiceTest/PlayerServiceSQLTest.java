package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.ServiceTest;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.enums.Role;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.DiceRollRepository;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.services.PlayerServiceSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerServiceSQLTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private DiceRollRepository diceRollRepository;

    @InjectMocks
    private PlayerServiceSQL playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("PlayerServiceSQLTest - Test return all players")
    @Test
    void testGetAllPlayersDTO() {
        List<Player> players = new ArrayList<>();
        players.add(Player.builder().id(1L).email("test1@example.com").password("password").nick("test1").role(Role.USER).winRate(0).build());
        players.add(Player.builder().id(2L).email("test2@example.com").password("password").nick("test2").role(Role.USER).winRate(0).build());

        when(playerRepository.findAllByOrderById()).thenReturn(players);

        List<PlayerDTO> playerDTOs = playerService.getAllPlayersDTO();

        assertEquals(players.size(), playerDTOs.size());
    }

    @DisplayName("PlayerServiceSQLTest - Test return player by ID")
    @Test
    void testGetPlayerByIdDTO() {
        Long playerId = 1L;
        Player player = Player.builder().id(playerId).email("test@example.com").password("password").nick("testnick").role(Role.USER).winRate(0).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        PlayerDTO playerDTO = playerService.getPlayerByIdDTO(playerId);

        assertEquals(player.getId(), playerDTO.getId());
        assertEquals(player.getEmail(), playerDTO.getEmail());
    }

    @DisplayName("PlayerServiceSQLTest - Test return player by ID - Player not found")
    @Test
    void testGetPlayerByIdDTO_NotFound() {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> {
            playerService.getPlayerByIdDTO(playerId);
        });
    }

    @DisplayName("PlayerServiceSQLTest - Test update player")
    @Test
    void testUpdatePlayer() {
        Long playerId = 1L;
        Player existingPlayer = Player.builder().id(playerId).email("test@example.com").password("password").nick("oldNick").role(Role.USER).winRate(0).build();
        Player updatedPlayer = Player.builder().id(playerId).email("test@example.com").password("password").nick("newNick").role(Role.USER).winRate(0).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.save(existingPlayer)).thenReturn(updatedPlayer);

        PlayerDTO updatedPlayerDTO = playerService.updatePlayer(playerId, updatedPlayer);

        assertEquals(updatedPlayer.getNick(), updatedPlayerDTO.getNick());
    }

    @DisplayName("PlayerServiceSQLTest - Test update player - Return Player not found")
    @Test
    void testUpdatePlayer_NotFound() {
        Long playerId = 1L;
        Player updatedPlayer = Player.builder().id(playerId).email("test@example.com").password("password").nick("newNick").role(Role.USER).winRate(0).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> {
            playerService.updatePlayer(playerId, updatedPlayer);
        });
    }

    @DisplayName("PlayerServiceSQLTest - Test delete player by ID")
    @Test
    void testDeletePlayerById() {
        Long playerId = 1L;
        Player existingPlayer = Player.builder().id(playerId).email("test@example.com").password("password").nick("testnick").role(Role.USER).winRate(0).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        doNothing().when(playerRepository).deleteById(playerId);

        playerService.deletePlayerById(playerId);

        verify(playerRepository, times(1)).deleteById(playerId);
    }

    @DisplayName("PlayerServiceSQLTest - Test delete player by ID - Return Player not found")
    @Test
    void testDeletePlayerById_NotFound() {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> {
            playerService.deletePlayerById(playerId);
        });
    }
}
