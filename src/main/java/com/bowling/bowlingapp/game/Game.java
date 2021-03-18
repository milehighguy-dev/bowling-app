package com.bowling.bowlingapp.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A single bowling game
 *
 */
@Entity
@Table( name = "games")
public class Game {

     @Id @GeneratedValue
//     @JsonIgnore
     private Long id;

     @OneToMany(cascade = CascadeType.ALL,
             fetch = FetchType.LAZY,
             mappedBy = "game")
     @JsonManagedReference
     List<Player> players;

     @Column
     private boolean isOver = false;

    //required default constructor for JSON mapping
    public Game() {
    }

    public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public List<Player> getPlayers() {
          return players;
     }

     public void setPlayers(List<Player> players) {
          this.players = players;
     }

     public void addPlayer(Player player) {
          if (this.players == null) {
               players = new ArrayList<>();
          }
          this.players = players;
     }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }
}
