package com.tobs.anotador.backend;

import java.util.*;

public abstract class Scorekeeper {

    private List<List<String>> players;
    private Integer roundsCount = 0;
    private Integer roundsLimit;

    /**
     * Lets the user get the score of the player with the given index.
     *
     * @param playerIndex The index of the player.
     * @return The score of the player with the given index.
     */
    public String getScore(int playerIndex) {
        if(roundsCount == 0) {
            return "0";
        }
        return players.get(playerIndex).get(roundsCount / players.size() + 1);
    }

    /**
     * Lets the user get the score of the player with the given index in a given position/cell.
     *
     * @param playerIndex The index of the player.
     * @param position The position/cell of the grid.
     * @return The score of the player with the given index in the given position.
     */
    public String getScore(int playerIndex, int position) {
        return players.get(playerIndex).get(position);
    }

    /**
     * Lets the user get the list of players.
     *
     * @return The list of players.
     */
    public List<String> getPlayers() {
        List<String> names = new ArrayList<>();
        for (List<String> player : players) {
            names.add(player.get(0));
        }
        return names;
    }

    /**
     * Lets the user get the number of columns in the grid.
     *
     * @return The number of columns in the grid.
     */
    public Integer getColumns() {
        return players.size();
    }

    /**
     * Lets the user get the number of rows in the grid.
     *
     * @return The number of rows in the grid.
     */
    public Integer getRows() {
        return players.get(0).size();
    }

    /**
     * Lets the user get the number of rounds played.
     *
     * @return The number of rounds played.
     */
    public Integer getRoundsCount() {
        return roundsCount;
    }

    /**
     * Lets the user get the number of rounds limit.
     *
     * @return The number of rounds limit.
     */
    public Integer getRoundsLimit() {
        return roundsLimit;
    }

    /**
     * Lets the user add the given score to the player with the given index.
     *
     * @param playerIndex The index of the player.
     * @param newScore The score to add.
     * @return always false.
     */
    public boolean addScore(int playerIndex, Integer newScore) {
        Integer previousScore = Integer.parseInt(getScore(playerIndex));
        this.setScore(playerIndex, previousScore + newScore);

        return false;
    }

    /**
     * Lets the user set the score of the player with the given index.
     *
     * @param playerIndex The index of the player.
     * @param newScore The score to set.
     */
    public void setScore(int playerIndex, Integer newScore) {
        players.get(playerIndex).add(Integer.toString(newScore));
        roundsCount++;
    }

    /**
     * Lets the user set the score of the player with the given index in a given position/cell.
     *
     * @param playerIndex The index of the player.
     * @param position The position/cell of the grid.
     * @param newScore The score to set.
     */
    public void setScore(int playerIndex, int position, Integer newScore) {
        players.get(playerIndex).set(position, Integer.toString(newScore));
        roundsCount++;
    }

    /**
     * Lets the user set the list of players.
     *
     * @param players The list of players.
     */
    public void setPlayers(List<List<String>> players) {
        this.players = players;
    }

    /**
     * Lets the user set the number of rounds limit.
     *
     * @param roundsLimit The number of rounds limit.
     */
    public void setRoundsLimit(Integer roundsLimit) {
        this.roundsLimit = roundsLimit;
    }

    /**
     * Lets the user decrement by 1 the number of rounds played.
     */
    public void decRoundsCount() {
        roundsCount--;
    }
}
