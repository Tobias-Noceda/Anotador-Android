package com.tobs.anotador.backend;

import androidx.annotation.NonNull;

/**
 * Represents a player of the game.
 */
public class Player implements Comparable<Player> {

    private final Integer id;
    private final String name;

    public Player(Integer id, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Player other) {
        return id.compareTo(other.id);
    }

    /**
     * Lets the user get the name of the player.
     *
     * @return The name of the player.
     */
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
