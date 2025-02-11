package com.tobs.anotador.backend;

import java.util.*;

public class Cocktail extends Scorekeeper {
    private final List<Integer> playedRounds = new ArrayList<>();

    /**
     * Creates a new Carioca scorekeeper. Setting the grid of scores and the cash count of each player.
     * @param players The players of the game.
     */
    public Cocktail(Collection<Player> players) {
        setRoundsLimit(6);
        List<List<String>> playersList = new ArrayList<>();

        playersList.add(List.of("Ronda", "Bazas", "♦♦♦", "Q's", "7♠ y UB", "\uD80C\uDE8D\uD80C\uDE8D\uD80C\uDE8D\uD80C\uDE8D", "\uD83C\uDF78\uD83C\uDF78"));
        for (Player player : players) {
            List<String> playerScores = new ArrayList<>();
            playerScores.add(player.getName());
            playersList.add(player.getId(), playerScores);
            playedRounds.add(0);
        }
        setPlayers(playersList);
    }

    /**
     * Lets the user get the number of played rounds of the player with the given index.
     * @param playerIndex The index of the player.y
     *
     * @return The number of played rounds of the player with the given index.
     */
    public int getPlayedRounds(int playerIndex) {
        return playedRounds.get(playerIndex - 1);
    }

    /**
     * Lets the user get the score of the player with the given index.
     * @param playerIndex The index of the player.
     *
     * @return The score of the player with the given index.
     */
    @Override
    public String getScore(int playerIndex) {
        if(playedRounds.get(playerIndex - 1) == 0) {
            return "0";
        }
        return getScore(playerIndex, playedRounds.get(playerIndex - 1));
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
        playedRounds.set(playerIndex - 1, playedRounds.get(playerIndex - 1) + 1);
        return getRoundsCount() % (getColumns() - 1) == 0;
    }

    @Override
    public void setScore(int playerIndex, int position, Integer newScore) {
        super.setScore(playerIndex, position, newScore);
        decRoundsCount();
    }
}
