package com.bowling.bowlingapp.game;

import com.bowling.bowlingapp.controller.NotPossibleException;

import java.util.*;

/**
 * A class to put most of the game procedural logic into
 *
 * //TODO figure out the remaining bugs...
 */
public class GameLogic {

    private final static int NUMBER_OF_PINS = 10;
    private final static int MAX_TURN = 10;

    public static void doRoll(Player player, int rollScore) {

        List<Frame> frames = player.getFrames();
        int frameSize = frames.size();

        if (frameSize > 10 ) {
            throw new NotPossibleException("The frame cannot go to " + 11 + ". The game is over");
        }

        //if this is the first frame and first roll for that player
        if (frameSize == 0) {

            Frame firstFrame = makeNextFrame(player);
            firstFrame.setFirstRoll(rollScore);
            frames.add(firstFrame);

            // figure out the latest roll
        } else {

            Frame latestFrame = frames.get(frameSize-1);

            boolean moveToNextFrame = false;
            boolean doSecondRoll = false;
            boolean doThirdRoll = false;

            if (latestFrame.getFirstRoll() < 0) {

                latestFrame.setFirstRoll(rollScore);

                if (latestFrame.getWasStrike()) {
                    if (frameSize < MAX_TURN) {
                        moveToNextFrame = true;
                    } else {
                        doThirdRoll = true;
                    }
                }

            } else {

                 if (latestFrame.getSecondRoll() < 0) {

                    if ((rollScore + latestFrame.getFirstRoll()) > NUMBER_OF_PINS) {
                        throw new NotPossibleException("Cannot roll more than "
                                + (NUMBER_OF_PINS - latestFrame.getFirstRoll()) + " remaining pins. " + rollScore + " attempted");
                    }

                    latestFrame.setSecondRoll(rollScore);

                    if (latestFrame.getWasSpare()) {
                        if (frameSize < MAX_TURN) {
                            moveToNextFrame = true;
                        } else {
                            doThirdRoll = true;
                        }
                    } else {
                        //score normal roll
//                    latestFrame.setScore(latestFrame.getFirstRoll() + latestFrame.getSecondRoll());
                        moveToNextFrame = true;
                    }
                }
            }

            //this should happen on the 10th turn when there was a spare or a strike
            if (doThirdRoll) {
                latestFrame.setThirdRoll(rollScore);
            }

            //score the unscored frames
            for (int i = 0; i < frameSize; i++) {

                Frame frame = frames.get(i);

                if (!frame.getWasScored()) {

                    //special logic for last frame
                    if (frameSize >= MAX_TURN ) {

                        //one strike permits 2 more rolls on third frame
                        if (frame.getFirstRoll() == NUMBER_OF_PINS) {
                            frame.setScore(frame.getFirstRoll() + frame.getSecondRoll() + frame.getThirdRoll());
                            //if there is another strike or a spare they get a third roll
                        } else if (frame.getFirstRoll() + frame.getSecondRoll() >= NUMBER_OF_PINS) {
                            frame.setScore(frame.getFirstRoll() + frame.getSecondRoll() + frame.getThirdRoll());
                        } else {
                            frame.setScore(frame.getFirstRoll() + frame.getSecondRoll());
                        }

                    } else {



                        if (i + 1 < frameSize) {

                            Frame oneFrameAhead = frames.get(i + 1);

                            //score spare
                            if (frame.getWasSpare()) {
                                //score is 10 + next 1 rolls (no matter the frame)
                                if (oneFrameAhead.getFirstRoll() > -1) {
                                    frame.setScore(NUMBER_OF_PINS + oneFrameAhead.getFirstRoll());
                                }

                            //score the strike
                            } else if (frame.getWasStrike()) {
                                //score is 10 + next 2 rolls (no matter the frame)
                                int score = NUMBER_OF_PINS;
                                if (!oneFrameAhead.getWasStrike() && oneFrameAhead.getSecondRoll() > -1) {
                                    //if it was not a strike then we just add up the 2 rolls
                                    score += oneFrameAhead.getFirstRoll() + oneFrameAhead.getSecondRoll();
                                    frame.setScore(score);
                                } else {
                                    //if it was a strike we have to go one frame further

                                    if (i + 2 < frameSize) {
                                        Frame secondFrameAhead = frames.get(i + 2);
                                        if (secondFrameAhead.getFirstRoll() > -1) {
                                            score += oneFrameAhead.getFirstRoll() + secondFrameAhead.getFirstRoll();
                                            frame.setScore(score);
                                        }
                                    }
                                }
                            }
                        }

                        if (!frame.getWasStrike() && !frame.getWasSpare() && frame.getSecondRoll() > -1) {
                            frame.setScore(frame.getFirstRoll() + frame.getSecondRoll());
                        }
                    }
                } // end if didn't already have a score
            } // end score loop

            if (moveToNextFrame) {
                frames.add(makeNextFrame(player));
                frameSize = frames.size();
            }
        }
    }

    /**
     * make a new frame when the game is just beginning or the prev frame has ended
     *
     * @param player
     * @return
     */
    private static Frame makeNextFrame(Player player) {
        Frame nextFrame = new Frame();
        nextFrame.setPlayer(player);

        return nextFrame;
    }

    /**
     *
     * return the player with the highest score or else return null
     *
     * @param game
     * @return
     */
    public static Player getWinnerIfPresent(Game game) {

        Map<Player,Integer> scoreMap = new HashMap<>();

        for (Player player : game.getPlayers()) {
            int numFrames = player.getFrames().size();
            if (numFrames == 10
            && player.getFrames().get(numFrames -1).getWasScored()) {
                scoreMap.put(player,player.getFrames().get(numFrames -1).getScore());
            }

        }

        //set the winner
        Optional<Map.Entry<Player, Integer>> entry = scoreMap.entrySet().stream().max(Map.Entry.comparingByValue());
        if (entry.isPresent()) {
            entry.get().getKey().setWinner(true);
            game.setOver(true);
            return entry.get().getKey();
        }

        return null;
    }

}
