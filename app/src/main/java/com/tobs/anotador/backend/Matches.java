package com.tobs.anotador.backend;

import java.util.*;

public class Matches extends Scorekeeper {

    /**
     * Creates a new Matches scorekeeper. Setting the grid of scores and the number of rounds limit.
     *
     * @param players The players of the game.
     * @param limit The number of rounds limit.
     */
    public Matches(Collection<Player> players, int limit) {
        List<List<String>> playersList = new ArrayList<>();
        for (Player player : players) {
            List<String> lista = new ArrayList<>();
            lista.add(player.getName());
            lista.add("0");
            playersList.add(player.getId() - 1, lista);
        }

        setPlayers(playersList);
        setRoundsLimit(limit);
    }

    /**
     * Lets the user get the score of the player with the given index.
     * @param playerIndex The index of the player.
     *
     * @return The score of the player with the given index.
     */
    @Override
    public String getScore(int playerIndex) {
        return getScore(playerIndex, 1);
    }

    /**
     * Lets the user add the given score to the player with the given index.
     * @param playerIndex The index of the player.
     * @param newScore The score to add.
     *
     * @return true if the player with the given index has reached the points limit, false otherwise.
     */
    @Override
    public boolean addScore(int playerIndex, Integer newScore) {
        Integer score = Integer.parseInt(getScore(playerIndex));
        if(score == 0 && newScore < 0) {
            score = 0;
        } else {
            score += newScore;
        }
        setScore(playerIndex, 1, score);

        return score >= getRoundsLimit();
    }
}
