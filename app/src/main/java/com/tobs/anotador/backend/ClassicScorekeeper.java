package com.tobs.anotador.backend;

import java.util.*;

public class ClassicScorekeeper extends Scorekeeper {

    private final List<Integer> playedRounds = new ArrayList<>();

    /**
     * Creates a new Classic scorekeeper. Setting the grid of scores and the number of played rounds of each player.
     * @param players The players of the game.
     */
    public ClassicScorekeeper(Collection<Player> players) {
        List<List<String>> playersList = new ArrayList<>();

        for (Player player : players) {
            List<String> playerScores = new ArrayList<>();
            playerScores.add(player.getName());
            playersList.add(player.getId() - 1, playerScores);
            playedRounds.add(0);
        }

        setPlayers(playersList);
    }

    /**
     * Lets the user get the number of played rounds of the player with the given index.
     * @param playerIndex The index of the player.
     *
     * @return The number of played rounds of the player with the given index.
     */
    public int getPlayedRounds(int playerIndex) {
        return playedRounds.get(playerIndex);
    }

    public void incPlayedRounds(int playerIndex) {
        playedRounds.set(playerIndex, getPlayedRounds(playerIndex) + 1);
    }

    /**
     * Lets the user get the score of the player with the given index.
     * @param playerIndex The index of the player.
     *
     * @return The score of the player with the given index.
     */
    @Override
    public String getScore(int playerIndex) {
        if(playedRounds.get(playerIndex) == 0) {
            return "0";
        }
        return getScore(playerIndex, playedRounds.get(playerIndex));
    }

    /**
     * Lets the player with the given index add the given score to his total score.
     * @param playerIndex The index of the player.
     * @param newScore The score to add.
     *
     * @return True if all the rounds have been played, false otherwise.
     */
    @Override
    public boolean addScore(int playerIndex, Integer newScore) {
        super.addScore(playerIndex, newScore);
        playedRounds.set(playerIndex, playedRounds.get(playerIndex) + 1);
        return getRoundsCount() % getColumns() == 0;
    }

    @Override
    public void setScore(int playerIndex, int position, Integer newScore) {
        super.setScore(playerIndex, position, newScore);
        decRoundsCount();
    }
}
