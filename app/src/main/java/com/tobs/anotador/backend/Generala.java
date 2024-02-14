package com.tobs.anotador.backend;

import java.util.*;

public class Generala extends Scorekeeper {

    private static final int PLAYER_ROUNDS = 11;

    /**
     * Creates a new Generala scorekeeper. Setting the grid of scores.
     *
     * @param players The players of the game.
     * @param playerCount The number of players of the game.
     */
    public Generala(Collection<Player> players, int playerCount) {
        setRoundsLimit(PLAYER_ROUNDS * playerCount);
        List<List<String>> playersList = new ArrayList<>(playerCount + 1);

        playersList.add(roundsColumn());
        SortedSet<Player> sortedPlayers = new TreeSet<>(players);
        for (Player player : sortedPlayers) {
            List<String> playerScores = getEmptyPlayerScores(player.toString());
            playersList.add(playerScores);
        }
        setPlayers(playersList);
    }

    @Override
    public String getScore(int playerIndex) {
        return getScore(playerIndex, 12);
    }

    /**
     * Lets the user add the given score to the player with the given index in the given round.
     * @param playerIndex The index of the player.
     * @param newScore The score to add.
     *
     * @return always false.
     */
    @Override
    public boolean addScore(int playerIndex, Integer newScore) {
        int previousScore = Integer.parseInt(getScore(playerIndex, getRows() - 1));
        setScore(playerIndex, getRows() - 1, previousScore + newScore);
        decRoundsCount();

        return getRoundsCount().equals(getRoundsLimit());
    }


    /**
     * Builds the first column of the grid of scores with the rounds.
     *
     * @return The first column of the grid of scores with the rounds.
     */
    private List<String> roundsColumn() {
        List<String> fila = new ArrayList<>();
        fila.add("");
        for (int i = 1; i <= 6; i++) {
            fila.add(Integer.toString(i));
        }
        fila.add("E");
        fila.add("F");
        fila.add("P");
        fila.add("G");
        fila.add("2G");
        fila.add("Tot");
        return fila;
    }

    /**
     * Builds the column for a player starting with his name.
     * @param playerName The name of the player.
     *
     * @return An empty list of scores for a player.
     */
    private List<String> getEmptyPlayerScores(String playerName) {
        List<String> playerScores = new ArrayList<>();
        playerScores.add(playerName);
        for (int i = 0; i < PLAYER_ROUNDS; i++) {
            playerScores.add("");
        }
        playerScores.add("0");
        return playerScores;
    }

}
