package com.tobs.anotador.backend;

import java.util.*;

public class Carioca extends Scorekeeper {

    private final List<Integer> cashCounts = new ArrayList<>();
    private final List<Integer> playedRounds = new ArrayList<>();

    /**
     * Creates a new Carioca scorekeeper. Setting the grid of scores and the cash count of each player.
     * @param players The players of the game.
     * @param startingCash The starting cash of each player.
     */
    public Carioca(Collection<Player> players, int startingCash) {
        setRoundsLimit(8);
        List<List<String>> playersList = new ArrayList<>();

        playersList.add(List.of("Ronda", "2P (7)", "1P1E (8)", "2E (9)", "3P (10)", "2P1E (11)", "1P2E (12)", "3E (13)", "4P (13)"));
        for (Player player : players) {
            List<String> playerScores = new ArrayList<>();
            playerScores.add(player.getName());
            playersList.add(player.getId(), playerScores);
            playedRounds.add(0);
            if(startingCash > 0) {
                cashCounts.add(startingCash);
            }
        }
        setPlayers(playersList);
    }

    /**
     * Lets the user get the cash count of the player with the given index.
     * @param playerIndex The index of the player.
     *
     * @return The cash count of the player with the given index.
     */
    public int getCashCount(int playerIndex) {
        return cashCounts.get(playerIndex - 1);
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
     * Lets the player with the given index add 1 to his cash count.
     * @param playerIndex The index of the player.
     */
    public void addCash(int playerIndex) {
        cashCounts.set(playerIndex - 1, cashCounts.get(playerIndex - 1) + 1);
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
