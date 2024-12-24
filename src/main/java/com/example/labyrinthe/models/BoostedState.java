package com.example.labyrinthe.models;

public class BoostedState implements PlayerState {
    @Override
    public void move(Player player) {
        System.out.println("Joueur boosté !");
        player.setSpeed(4); // Exemple : vitesse rapide
    }
}