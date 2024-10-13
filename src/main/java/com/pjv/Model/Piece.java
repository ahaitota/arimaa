package com.pjv.Model;

/**
 * The Piece class represents a game piece in Arimaa.
 */

public class Piece {
    private Pieces type;
    private Colors color; //golden, silver

     /**
     * The Pieces enum represents the types of pieces and thier strength
     * and notation.
     */

    public enum Pieces {
        ELEPHANT(6, 'E', 'e'),
        CAMEL(5, 'M', 'm'),
        HORSE(4, 'H', 'h'),
        DOG(3, 'D', 'd'),
        CAT(2, 'C', 'c'),
        RABBIT(1, 'R', 'r');

        private final int strength;
        private final char goldNotation;
        private final char silverNotation;

        /**
         * Constructs a Pieces enum with the specified strength and notations.
         *
         * @param strength       the strength of the piece
         * @param goldNotation   the notation of the piece for gold player
         * @param silverNotation the notation of the piece for silver player
         */

        Pieces(int strength, char goldNotation, char silverNotation) {
            this.strength = strength;
            this.goldNotation = goldNotation;
            this.silverNotation = silverNotation;
        }

        public int getStrength() {
            return strength;
        }

        /**
         * Returns the notation of the piece based on the specified color.
         *
         * @param color the color of the piece
         * @return the notation of the piece
         */

        public char getNotation(Colors color) {
            return (color == Colors.GOLD) ? goldNotation : silverNotation;
        }

         /**
         * Converts the notation to a Pieces enum.
         *
         * @param notation the notation of the piece
         * @return the corresponding Pieces enum
         */

        public static Pieces fromNotation(char notation) {
            for (Pieces piece : Pieces.values()) {
                if (piece.goldNotation == notation || piece.silverNotation == notation) {
                    return piece;
                }
            }
            return null;
        }
    }

    /**
     * The Colors enum represents colors of pieces.
     */

    public enum Colors {
        GOLD, SILVER;
    }

    public Piece(Pieces type, Colors color) {
        this.type = type;
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public Pieces getType() {
        return type;
    }

    public char getNotation() {
        return type.getNotation(color);
    }

}
