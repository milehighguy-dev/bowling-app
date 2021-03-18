package com.bowling.bowlingapp.game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A bowler
 *
 */
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column
    private String name;

    @Column
    private boolean isWinner = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @JsonBackReference
    private Game game;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "player")
    @JsonManagedReference
    List<Frame> frames;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Frame> getFrames() {
        if (frames == null) {
            frames = new ArrayList<>();
        }
        return frames;
    }

    @JsonIgnore
    public Frame getLatestFrame() {

        int frameListSize = frames.size();

        if (frameListSize > 0 ) {
            return frames.get(frameListSize - 1 );
        } else {
            return null;
        }

    }

    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }
}
