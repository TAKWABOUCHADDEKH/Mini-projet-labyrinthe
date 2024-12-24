package com.example.labyrinthe.models;

public class SlowedState implements PlayerState {
    @Override
    public void move(Player player) {
        System.out.println("Joueur ralenti.");
        player.setSpeed(1); // Exemple : vitesse lente
    }
}
