package com.bowling.bowlingapp.controller;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import com.bowling.bowlingapp.db.GameRepository;
import com.bowling.bowlingapp.game.FrameUpdate;
import com.bowling.bowlingapp.game.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BowlingControllerIntegrationTest {

    //spring discovers the local port
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");

    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
    }

    @Test
    public void testHello() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody()).isEqualTo("Hello bowler");
    }

    @Test
    public void testNewGameCreated() throws IOException {

        Game game = new ObjectMapper().readValue( this.getClass().getResourceAsStream("/newGame.json"), Game.class);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Game> request = new HttpEntity<>(game,headers);

        URL newGameUrl = new URL(base.toString() + "game");

        ResponseEntity<String> response = template.postForEntity(newGameUrl.toString(), request, String.class);

        Game responseGame = new ObjectMapper().readValue(response.getBody(), Game.class);

        assertThat(responseGame.getId().equals(game.getId()));

        Optional<Game> loadedGame = gameRepository.findById(game.getId());

        assertThat(loadedGame.isPresent());

        assertThat(loadedGame.get().getId().equals(game.getId()));

    }


    @Test
    public void testCannotBowlMoreThanTenPins() throws IOException {

        Game game = new ObjectMapper().readValue( this.getClass().getResourceAsStream("/newGame.json"), Game.class);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Game> newGameRequest = new HttpEntity<>(game,headers);
        URL newGameUrl = new URL(base.toString() + "game");
        ResponseEntity<String> gameResponse = template.postForEntity(newGameUrl.toString(), newGameRequest, String.class);

        assertThat(gameResponse.getStatusCode() == HttpStatus.OK);

        FrameUpdate frameUpdate = new FrameUpdate();
        frameUpdate.setGameId(game.getId());
        frameUpdate.setPlayerName(game.getPlayers().get(0).getName());
        frameUpdate.setRollScore(43);

        HttpEntity<FrameUpdate> rollRequest = new HttpEntity<>(frameUpdate,headers);
        URL newFrameUrl = new URL(base.toString() + "frame");

        ResponseEntity<String> rollResponse = template.postForEntity(newFrameUrl.toString(), rollRequest, String.class);

        System.out.println(rollResponse.getBody());

        assertThat(rollResponse.getStatusCode() == HttpStatus.BAD_REQUEST);

    }
}
