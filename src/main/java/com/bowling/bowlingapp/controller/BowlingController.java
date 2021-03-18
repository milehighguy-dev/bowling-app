package com.bowling.bowlingapp.controller;

import com.bowling.bowlingapp.db.FrameRepository;
import com.bowling.bowlingapp.db.GameRepository;
import com.bowling.bowlingapp.db.PlayerRepository;
import com.bowling.bowlingapp.game.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller for bowling games to be created via HTTP requests with JSON
 *
 * See test JSON files in the src/test/resources folder
 *
 * Games are started with a new Game posted the the /game endpoint
 *
 * Each roll is entered with a FrameUpdate object posted the the /frame endpoint
 *
 */
@RestController
public class BowlingController {

    @Autowired
    private  GameRepository gameRepository;

    //only need the game repo
    @Autowired
    private  PlayerRepository playerRepository;
    @Autowired
    private FrameRepository frameRepository;

    @RequestMapping("/")
    public String hello() {
        return "Hello bowler";
    }

    @PostMapping("/game")
    public Game newGame(@RequestBody Game game) {
        //create new game

        return gameRepository.save(game);
    }

    @GetMapping("/games")
    public List<Game> allGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/game/{id}")
    public Game getGame(@PathVariable Long id) {

        return gameRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Game with Id " + id + " not found"));
    }

    @DeleteMapping("/game/{id}")
    public void deleteGame(@PathVariable Long id) {

        gameRepository.deleteById(id);
    }

    @PostMapping("/frame")
    public ResponseEntity<Game> updatePlayerFrame(@RequestBody FrameUpdate frameUpdate) {

        //add the roll to the frame
        System.out.println("hit this");

        int rollScore = frameUpdate.getRollScore();

        if (rollScore > 10 ) {
            throw new NotPossibleException("You cannot bowl more than 10 pins! You tried " + rollScore);
        }

        long gameId = frameUpdate.getGameId();

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ItemNotFoundException("Game with Id " + gameId + " not found"));

        String playerName = frameUpdate.getPlayerName();
        List<Player> players = game.getPlayers();

        if (players.stream().noneMatch(player -> player.getName().equals(playerName))) {
            throw new ItemNotFoundException("Player with name " + playerName + " not found in game " + gameId);
        }

        //add the latest roll
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                GameLogic.doRoll(player, rollScore);
                break;
            }
        }

        Player winner = GameLogic.getWinnerIfPresent(game);
        if (winner != null) {
            System.out.println("The game is now over. Player " + winner.getName() + " is the Winner of " + game.getId());
            //maybe throw exception?
        }

        game = gameRepository.save(game);

        return ResponseEntity.ok(game);
    }

    @GetMapping("/getFrame")
    public String getFrameUpdate() {
     FrameUpdate frameUpdate = new FrameUpdate();
     frameUpdate.setGameId(1l);
     frameUpdate.setPlayerName("Ben");
     Frame frame = new Frame();
     frameUpdate.setRollScore(6);

        ObjectMapper mapper = new JsonMapper();
        String result = "temp";
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(frameUpdate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("/makeGame")
    public String makeGame() {

        Player bob = new Player("Bob");
        Player joe = new Player("Joe");
        Frame frameOne = new Frame();
        frameOne.setScore(12);
        frameOne.setFirstRoll(1);
        Frame frameTwo = new Frame();
        frameTwo.setFirstRoll(2);
        frameTwo.setScore(15);
        List<Frame> frames = new ArrayList<>();
        frames.add(frameOne);
        frames.add(frameTwo);
        bob.setFrames(frames);
        joe.setFrames(frames);

        List<Player> players = new ArrayList<>();
        players.add(bob);
        players.add(joe);
        Game game = new Game();
        game.setPlayers(players);

        ObjectMapper mapper = new JsonMapper();
        String result = "temp";
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(game);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

}
