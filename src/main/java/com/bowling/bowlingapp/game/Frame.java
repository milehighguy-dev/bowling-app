package com.bowling.bowlingapp.game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;

/**
 * an object representing a frame.
 * Each value will be -1 until set
 *
 */
@Entity
@Table(name = "frames")
public class Frame {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column
    private int firstRoll = -1;
    @Column
    private int secondRoll = -1;
    //3rd roll for tenth frame
    @Column
    private int thirdRoll = -1;
    @Column
    private int score = -1;

    @Column
    private boolean done = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Player player;

    public Frame() {
    }

    @JsonIgnore
    public boolean getWasScored() {
        return score > -1;
    }

    @JsonIgnore
    public boolean getWasStrike() {
        return firstRoll == 10;
    }

    @JsonIgnore
    public boolean getWasSpare() {
        return firstRoll + secondRoll == 10;
    }

    public void setFirstRoll(int firstRoll) {
        this.firstRoll = firstRoll;
    }



    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFirstRoll() {
        return firstRoll;
    }

    public int getSecondRoll() {
        return secondRoll;
    }

    public void setSecondRoll(int secondRoll) {
        this.secondRoll = secondRoll;
    }

    public int getThirdRoll() {
        return thirdRoll;
    }

    public void setThirdRoll(int thirdRoll) {
        this.thirdRoll = thirdRoll;
    }

    public int getScore() {
        return score;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
