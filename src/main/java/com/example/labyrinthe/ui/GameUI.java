package com.example.labyrinthe.ui;


import com.example.labyrinthe.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameUI extends JFrame {

    private char[][] map; // Représentation du labyrinthe
    private int playerX, playerY; // Position du joueur
    private final int tileSize = 50; // Taille de chaque cellule
    private Timer timer; // Chronomètre
    private int timeLeft = 60; // Temps en secondes
    private Enemy[] enemies;
    private boolean hasKey = false; // Indique si le joueur possède une clé
    private int collectedBonuses = 0; // Nombre de bonus collectés
    private final int totalBonuses = 3; // Nombre total de bonus nécessaires

    private boolean isCaught = false; // Drapeau pour vérifier si le joueur a déjà été attrapé




    private void initEnemies() {
        enemies = new Enemy[] {
                new Enemy(2, 2),
                new Enemy(5, 7)
        };
    }

    private void startEnemyMovement() {
        Timer enemyTimer = new Timer(500, e -> {
            for (Enemy enemy : enemies) {
                enemy.move(map);
                if (enemy.isCollidingWithPlayer(playerX, playerY) && !isCaught) {
                    isCaught = true; // Marquer que le joueur a été attrapé
                    JOptionPane.showMessageDialog(this, "Vous avez été attrapé par un ennemi !");
                    System.exit(0); // Fermer le jeu après l'alerte
                }
            }
            repaint();
        });
        enemyTimer.start();
    }





    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            setTitle("Jeu de Labyrinthe - Temps restant : " + timeLeft + "s");
            if (timeLeft <= 0) {
                JOptionPane.showMessageDialog(this, "Temps écoulé ! Vous avez perdu !");
                System.exit(0);
            }
        });
        timer.start();
    }

    public GameUI() {
        // Charger le labyrinthe (Singleton)
        Labyrinthe labyrinthe = Labyrinthe.getInstance();
        this.map = labyrinthe.getMap();

        // Trouver la position initiale du joueur
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'P') {
                    playerX = j;
                    playerY = i;
                }
            }
        }

        // Configurer la fenêtre principale
        setTitle("Jeu de Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Ajouter un panneau pour dessiner le labyrinthe
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        // Calculer la taille correcte de la fenêtre
        pack(); // Ajuste le contenu pour déterminer les dimensions nécessaires
        Insets insets = getInsets(); // Obtient les marges de la fenêtre (bordures, titre, etc.)
        int windowWidth = map[0].length * tileSize + insets.left + insets.right;
        int windowHeight = map.length * tileSize + insets.top + insets.bottom;

        setSize(windowWidth, windowHeight); // Appliquer la taille ajustée

        // Ajouter un gestionnaire de clavier
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                movePlayer(e.getKeyCode());
                gamePanel.repaint(); // Redessiner après le déplacement
            }

            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });

        initEnemies();
        startEnemyMovement();


        setVisible(true);
        // Démarrer le chronomètre après avoir configuré la fenêtre
        startTimer();

    }

    // Méthode pour déplacer le joueur
    private void movePlayer(int keyCode) {
        int newX = playerX;
        int newY = playerY;

        // Calculer la position après le déplacement
        switch (keyCode) {
            case KeyEvent.VK_UP: newY--; break;
            case KeyEvent.VK_DOWN: newY++; break;
            case KeyEvent.VK_LEFT: newX--; break;
            case KeyEvent.VK_RIGHT: newX++; break;
        }

        // Vérifier que la nouvelle position est dans les limites du tableau
        if (newX < 0 || newX >= map[0].length || newY < 0 || newY >= map.length) {
            // Si la position est hors limites, ne faites rien
            System.out.println("Déplacement hors limites !");
            return;
        }

        // Vérifier la case où le joueur souhaite se déplacer
        char target = map[newY][newX];

        if (target == 'K') {
            hasKey = true; // Le joueur ramasse une clé
            JOptionPane.showMessageDialog(this, "Vous avez ramassé une clé !");
            map[newY][newX] = ' '; // La clé disparaît après avoir été ramassée
        } else if (target == 'D') {
            if (hasKey) {
                JOptionPane.showMessageDialog(this, "Vous avez ouvert la porte !");
                map[newY][newX] = ' '; // La porte devient un espace vide
            } else {
                JOptionPane.showMessageDialog(this, "Vous avez besoin d'une clé pour ouvrir cette porte.");
                return; // Le joueur ne bouge pas s'il n'a pas de clé
            }
        }

        // Déplacer le joueur si ce n'est pas un mur
        if (target != 'X'&& target != 'E') {
            map[playerY][playerX] = ' '; // Ancienne position devient vide
            playerX = newX;
            playerY = newY;
            map[playerY][playerX] = 'P'; // Nouvelle position
        }

        // Vérifier si le joueur entre en collision avec un ennemi
        for (Enemy enemy : enemies) {
            if (enemy.isCollidingWithPlayer(playerX, playerY) && !isCaught) {
                isCaught = true; // Marquer que le joueur a été attrapé
                JOptionPane.showMessageDialog(this, "Vous avez été attrapé par un ennemi !");
                System.exit(0); // Fermer le jeu après l'alerte
            }
        }


        // Vérifier si le joueur atteint la sortie
        if (target == 'B') {
            collectedBonuses++;
            //JOptionPane.showMessageDialog(this, "Bonus ramassé ! Bonus collectés : " + collectedBonuses + "/" + totalBonuses);
            showToast("Bonus ramassé ! Bonus collectés : " + collectedBonuses + "/" + totalBonuses);

            map[newY][newX] = ' '; // Le bonus disparaît après avoir été ramassé
        }

        if (target == 'E') {
            if (collectedBonuses == totalBonuses) {
                displayVictoryMessage();
            } else {
                JOptionPane.showMessageDialog(this, "Vous devez collecter tous les bonus avant de sortir !");
                return; // On empêche le joueur de se déplacer vers la sortie
            }
        }



    }

    public static void showToast(String message) {
        // Create a new frame to display the toast
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(300, 50);
        frame.setLocationRelativeTo(null);  // Center the frame

        // Create a label to show the message
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);

        frame.getContentPane().add(label);

        // Show the toast for a short duration (2 seconds)
        frame.setVisible(true);

        // Use a timer to hide the toast after 2 seconds
        Timer timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        timer.setRepeats(false);  // Only execute once
        timer.start();
    }


    // Nouvelle méthode : Afficher un message de victoire
    private void displayVictoryMessage() {
        // Changer la couleur de fond de la fenêtre (animation simple)
        getContentPane().setBackground(Color.GREEN);

        // Afficher un texte "Victoire !" au centre
        JOptionPane.showMessageDialog(this, "Félicitations ! Vous avez gagné !");

        // Quitter après un délai
        try {
            Thread.sleep(1000); // Pause d'une seconde avant de quitter
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0); // Fermer l'application
    }

    // Classe interne pour dessiner le labyrinthe
    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    // Choisir une couleur en fonction du contenu de la cellule
                    switch (map[i][j]) {
                        case 'X': g.setColor(Color.BLACK); break; // Mur
                        case 'P': g.setColor(Color.BLUE); break;  // Joueur
                        case 'E': g.setColor(Color.MAGENTA); break; // Sortie
                        case 'B': g.setColor(Color.GREEN); break; // Bonus
                        case 'K': g.setColor(Color.pink); break; // Clé
                        case 'D': g.setColor(Color.ORANGE); break;   // Porte
                        case 'M': g.setColor(Color.RED); break;   // Enemy
                        default: g.setColor(Color.WHITE); break;  // Vide
                    }
                    // Dessiner la cellule
                    g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

                    // Dessiner une bordure (facultatif)
                    g.setColor(Color.GRAY);
                    g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameUI();
    }
}