package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.repository;

import cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.domain.DiceRoll;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DiceRollRepository extends MongoRepository<DiceRoll, String> {

    Optional<DiceRoll> findById(String id);

    Optional<DiceRoll> findByPlayer(Long playerId);

    List<DiceRoll> findAllByPlayer(Long playerId);

    List<DiceRoll> findAllByOrderByPlayedOnDate();

    void deleteAllByPlayer(Long playerId);


}

