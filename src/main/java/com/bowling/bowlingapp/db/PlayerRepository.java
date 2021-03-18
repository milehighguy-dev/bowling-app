package com.bowling.bowlingapp.db;

import com.bowling.bowlingapp.game.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring JPA database solution to handle our CRUD requests
 *
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
