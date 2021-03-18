package com.bowling.bowlingapp.game;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    private Game game;
    private Player player1;
    private Player player2;
    private TestRollGenerator testRollGenerator1;
    private TestRollGenerator testRollGenerator2;

    @BeforeEach
    void setUp() {

        game = new Game();
        game.setId(1L);

        player1 = new Player("The Dude");
        player2 = new Player("Walter Sobchack");

        //probably not necessary for unit tests...
        player1.setGame(game);
        player2.setGame(game);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        game.setPlayers(players);

        testRollGenerator1 = new TestRollGenerator();
        testRollGenerator2 = new TestRollGenerator();
    }

    @AfterEach
    void tearDown() {
    }

    //TODO fix this.. There is something still wrong in the game procedure logic
    @Disabled
    @RepeatedTest(value = 10)
    void doRoll() {

        for (int i = 1; i < 20; i++) {

            GameLogic.doRoll(player1,testRollGenerator1.newRoll());
            GameLogic.doRoll(player2,testRollGenerator2.newRoll());


        }

        assertEquals(10, player1.getFrames().size());
        assertEquals(10, player1.getFrames().size());
    }

    /**
     * test if the winner is detected if they have the highest score on the tenth Frame
     */
    @Test
    void getWinnerIfPresent() {

        addTenFramesPlayer(player1);
        addTenFramesPlayer(player2);

        player1.getFrames().get(player1.getFrames().size()-1).setScore(8);
        player2.getFrames().get(player2.getFrames().size()-1).setScore(4);

        Player winnerPlayer = GameLogic.getWinnerIfPresent(game);

        assertEquals( player1.getName(), winnerPlayer.getName());
        assertTrue(player1.isWinner());
        assertTrue(game.isOver());

    }

    private void addTenFramesPlayer(Player player) {
        List<Frame> frames = new ArrayList<>();
        for( int i = 0; i < 10; i++) {
            Frame frame = new Frame();
            frame.setPlayer(player);
            frames.add(frame);
        }
        player.setFrames(frames);
    }
}