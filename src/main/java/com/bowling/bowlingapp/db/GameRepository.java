package com.bowling.bowlingapp.db;

import com.bowling.bowlingapp.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring JPA database solution to handle our CRUD requests
 *
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
