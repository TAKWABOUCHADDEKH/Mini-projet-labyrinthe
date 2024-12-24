package com.example.labyrinthe.models;

import java.util.Random;

public class Enemy {
    private int x, y;
    private final Random random = new Random();

    public Enemy(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move(char[][] map) {
        int newX = x;
        int newY = y;

        // Déplacement aléatoire : haut, bas, gauche ou droite
        switch (random.nextInt(4)) {
            case 0 -> newY--; // Haut
            case 1 -> newY++; // Bas
            case 2 -> newX--; // Gauche
            case 3 -> newX++; // Droite
        }

        // Vérifier si la nouvelle position est valide
        if (newY >= 0 && newY < map.length && newX >= 0 && newX < map[0].length && map[newY][newX] == ' ') {
            map[y][x] = ' '; // Ancienne position devient vide
            x = newX;
            y = newY;
            map[y][x] = 'M'; // 'M' pour marquer la position de l'ennemi
        }
    }

    public boolean isCollidingWithPlayer(int playerX, int playerY) {
        return this.x == playerX && this.y == playerY;
    }
}
