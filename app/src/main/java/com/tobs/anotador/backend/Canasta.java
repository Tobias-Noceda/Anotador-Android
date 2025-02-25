package com.tobs.anotador.backend;

import java.util.*;

public class Canasta extends ClassicScorekeeper {

    private final List<Integer> floor;
    private final int limit2, limit3;

    /**
     * Creates a new Canasta scorekeeper.
     * Limits are set to 1500, 3000 and 5000 and determine a change in the minimum score to start in the next round.
     * @param players The players of the game.
     * @param limit1 The first limit of the game.
     * @param limit2 The second limit of the game.
     * @param limit3 The third limit of the game.
     */
    public Canasta(Collection<Player> players, int limit1, int limit2, int limit3) {
        super(players, limit1);
        floor = new ArrayList<>(Collections.nCopies(players.size(), 50));
        this.limit2 = limit2;
        this.limit3 = limit3;
    }

    /**
     * Lets the user now the needed score to start in the next round of a player.
     * @param id The id of the player.
     *
     * @return The needed score to start in the next round of a player.
     */
    public int getFloor(int id) {
        return floor.get(id);
    }

    /**
     * Lets the player with the given id add the given score to his total score.
     * Also updates the floor of the player.
     * @param id The id of the player.
     * @param newScore The score to add.
     *
     * @return True if the player has reached the limit3, false otherwise.
     */
    @Override
    public boolean addScore(int id, Integer newScore) {
        Integer score = Integer.parseInt(getPlayedRounds(id) - 2 == 0 ? "0" : getScore(id, getPlayedRounds(id) - 2));
        score += newScore;
        super.setScore(id, score);
        incPlayedRounds(id);
        if(score >= getLimit() && score < limit2) {
            floor.set(id, 90);
        } else if(score >= limit2 && score < limit3) {
            floor.set(id, 120);
        } else return score >= limit3;

        return false;
    }

    @Override
    public void setScore(int playerIndex, Integer newScore) {
        super.setScore(playerIndex, newScore);
        incPlayedRounds(playerIndex);
    }

    /**
     * Returns the score of the player with the given id.
     * @param id The id of the player.
     *
     * @return The score of the player.
     */
    @Override
    public String getScore(int id) {
        return getScore(id, getPlayedRounds(id));
    }
}
