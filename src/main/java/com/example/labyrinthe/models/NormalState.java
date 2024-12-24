package com.example.labyrinthe.models;

public class NormalState implements PlayerState {
    @Override
    public void move(Player player) {
        System.out.println("Joueur en mode normal.");
        player.setSpeed(2); // Exemple : vitesse normale
    }
}