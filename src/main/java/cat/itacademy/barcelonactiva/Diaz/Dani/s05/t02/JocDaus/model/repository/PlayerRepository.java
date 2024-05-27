package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findUserByEmailIgnoreCase(String email);

    Optional<Player> findUserByNickIgnoreCase(String nick);

    List<Player> findAllByOrderById();
    List<Player> findAllByOrderByWinRateDesc();

    Player findTopByOrderByWinRateDesc();

    Player findTopByOrderByWinRateAsc();
    Player findPlayerById(Long id);

}
