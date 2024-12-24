package com.example.labyrinthe.models;


public class Player {
    private int x, y; // Position dans le labyrinthe
    private int speed; // Vitesse de déplacement
    private PlayerState state; // État du joueur

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.state = new NormalState(); // État initial
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void move(char direction) {
        state.move(this); // Applique l'effet de l'état sur la vitesse
        switch (direction) {
            case 'U': y -= speed; break; // Haut
            case 'D': y += speed; break; // Bas
            case 'L': x -= speed; break; // Gauche
            case 'R': x += speed; break; // Droite
        }
        System.out.println("Nouvelle position : (" + x + ", " + y + ")");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
