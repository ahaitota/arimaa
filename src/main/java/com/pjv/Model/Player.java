package com.pjv.Model;

/**
 * The Player class represents a player in the Arimaa game.
 */

public class Player {
    private Color color;
    //each reserv 5 mins - 300s
    private int playerReserve;

    /**
     * The Color enum represents the colors of players.
     */

    public enum Color {
        GOLD, SILVER;
    }

     /**
     * Constructs a Player object with the specified color and default reserve time.
     *
     * @param color the color of the player
     */

    public Player(Color color) {
        this.color = color;
        this.playerReserve = 300;
    }

    public int getPlayerReserve() {
        return playerReserve;
    }

    public void setPlayerReserve(int newReserve) {
        this.playerReserve = newReserve;
    }

    public Color getColor() {
        return color;
    }

}
