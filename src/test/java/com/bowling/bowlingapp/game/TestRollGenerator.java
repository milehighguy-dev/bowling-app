package com.bowling.bowlingapp.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * generates valid rolls for test purposes
 */
public class TestRollGenerator {

    private int standingPins = 10;
    private List<Integer> prevRolls = new ArrayList<>();

    public void setStandingPins(int pinCount) {
        standingPins = pinCount;
    }

    public int newRoll() {
        int roll = new Random().nextInt(standingPins + 1);

        standingPins = standingPins - roll;

        prevRolls.add(roll);
        if (prevRolls.size() >= 2) {
            standingPins = 10;
            prevRolls.clear();
        }

        if (standingPins <= 0) {
            standingPins = 10;
            prevRolls.clear();
        }

        System.out.println("rolling " + roll);
        return roll;
    }
}
