package com.example.labyrinthe.models;


public class Labyrinthe {
    private static Labyrinthe instance;
    private char[][] map; // Représentation du labyrinthe

    private Labyrinthe() {
        generateLabyrinthe();
    }

    public static Labyrinthe getInstance() {
        if (instance == null) {
            instance = new Labyrinthe();
        }
        return instance;
    }

    private void generateLabyrinthe() {
        map = new char[][] {
                {'P', ' ', 'X', ' ', ' ', ' ', 'X', ' ', ' ', 'E'},
                {'X', ' ', 'X', ' ', 'X', ' ', 'X', ' ', 'X', 'X'},
                {' ', ' ', ' ', 'B', ' ', ' ', 'X', ' ', ' ', ' '},
                {'X', 'X', ' ', 'X', 'X', ' ', 'X', 'X', 'X', ' '},
                {'B', ' ', ' ', 'K', 'X', ' ', ' ', 'D', ' ', ' '},
                {'X', ' ', 'X', ' ', 'X', ' ', 'X', 'X', 'X', 'X'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'B', ' ', ' '},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', ' ', 'X', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X', ' '},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', ' ', 'X', 'X'}

        };
    }

    public char[][] getMap() {
        return map;
    }
}
