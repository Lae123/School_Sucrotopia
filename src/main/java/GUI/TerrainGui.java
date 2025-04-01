package main.java.GUI;

import java.awt.*;

import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.*;


import main.java.model.*;


public class TerrainGui extends JFrame {

    private JComboBox<String> typeTourComboBox;
    private TerrainPanel terrainPanel;
    private Terrain terrain;
    private Jeu jeu;
    private int[] lastClickedCoordinates;
    private JPanel playerInfoPanel;
    private JLabel lifeLabel;
    private JLabel moneyLabel;
    private JLabel scoreLabel;
    private JLabel waveLabel;
    Dimension terrainPanelSize = this.getPreferredSize();
 

   
    public Jeu getJeu() {
        return jeu;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public TerrainGui(Terrain terrain, Jeu jeu) {
        this.terrain = terrain;
        this.jeu = jeu;
    
        setTitle("Sucrotopia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        terrainPanel = new TerrainPanel(terrain, this, jeu);
        add(terrainPanel, BorderLayout.CENTER);
    
        String[] typesTours = {"NOUNOURS_GUMMY", "FILAMENT_BARBAPAPA", "LANCE_TOPPING", "MARSHMALLOW_ENFLAMME"};
        typeTourComboBox = new JComboBox<>(typesTours);
    
        playerInfoPanel = new JPanel();
    
        playerInfoPanel.setPreferredSize(new Dimension(terrainPanelSize.width, playerInfoPanel.getPreferredSize().height * 4));
        playerInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
    
        playerInfoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        playerInfoPanel.setBackground(Color.LIGHT_GRAY);
    
        lifeLabel = createLabel("Vie: " + jeu.getJoueur().getVies());
        moneyLabel = createLabel("Argent: " + jeu.getJoueur().getArgent());
        scoreLabel = createLabel("Score: " + jeu.getJoueur().getScore());
        waveLabel = createLabel("Vague: " + jeu.getWaveManager().getCurrentWaveNumber());
    
        ImageIcon lifeIcon = new ImageIcon("bin/ressources/sprites/Tiles/coeur.png");
        ImageIcon moneyIcon = new ImageIcon("bin/ressources/sprites/Tiles/coin.png");
        ImageIcon scoreIcon = new ImageIcon("bin/ressources/sprites/Tiles/shield.png");
        ImageIcon waveIcon = new ImageIcon("bin/ressources/sprites/Tiles/wave.png");
    
        addLabelWithIcon(playerInfoPanel, lifeIcon, lifeLabel);
        addLabelWithIcon(playerInfoPanel, moneyIcon, moneyLabel);
        addLabelWithIcon(playerInfoPanel, scoreIcon, scoreLabel);
        addLabelWithIcon(playerInfoPanel, waveIcon, waveLabel);
    
        playerInfoPanel.setOpaque(false);
    
        add(playerInfoPanel, BorderLayout.NORTH);
    
       
    
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        controlPanel.add(new JLabel("Type de tour :"));

        EmptyBorder comboBoxBorder = new EmptyBorder(5, 5, 5, 5);
        typeTourComboBox.setBorder(comboBoxBorder);

        controlPanel.add(typeTourComboBox);

        add(controlPanel, BorderLayout.SOUTH);

        controlPanel.add(typeTourComboBox);
    
        JButton infoToursButton = new JButton("Infos Tours");
        infoToursButton.addActionListener(e -> showInfoTourDialog());
        controlPanel.add(infoToursButton);
    
        add(controlPanel, BorderLayout.SOUTH);
    
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void addLabelWithIcon(JPanel panel, ImageIcon icon, JLabel label) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = panel.getComponentCount() / 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
    
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel.add(iconLabel, gbc);
            gbc.gridx++;
        }
    
        panel.add(label, gbc);
    }
    
    
    private void showInfoTourDialog() {
        InfoTourDialog infoDialog = new InfoTourDialog(this);
        infoDialog.setVisible(true);
    }
    

    public void onPlacerTourButtonClick() {
        String selectedTourType = (String) typeTourComboBox.getSelectedItem();

        if (lastClickedCoordinates != null) {
            jeu.getJoueur().placerTourSurMapGui(terrain, selectedTourType, lastClickedCoordinates);
            lastClickedCoordinates = null;
            mettreAJourAffichage();
        } else {
            System.out.println("Les coordonnées de clic sont nulles.");
        }
    }


    protected void placerTourDepuisClic(int x, int y) {
        String selectedTourType = (String) typeTourComboBox.getSelectedItem();
        int[] coordonnees = jeu.getJoueur().demanderCoordonneesTourGui(x, y, terrain);

        if (coordonnees != null) {
            int cout = Tours.getPrixTour(selectedTourType);

            if (jeu.getJoueur().getArgent() >= cout) {

                System.out.println("argent du joueur : " + jeu.getJoueur().getArgent());
                System.out.println("cout de la tour : " + cout);

                jeu.getJoueur().placerTourSurMapGui(terrain, selectedTourType, coordonnees);
                int total = jeu.getJoueur().getArgent() - cout;
                jeu.getJoueur().setArgent(total);
                mettreAJourAffichage();

            } else {
                System.out.println("Pas assez d'argent pour placer la tour !");
            }
        } else {
            System.out.println("Coordonnées incorrectes ou emplacement occupé.");
        }
    }


    protected void ameliorerTourDepuisClic(int x, int y) {

        Cellule cellule = terrain.getCellule(y, x);
        
        if (cellule.getTour() != null) {
            if (cellule.getTour().getNiveau() < 3) {
                if (jeu.getJoueur().getArgent() >= cellule.getTour().getPrix()) {
                    jeu.getJoueur().ameliorerTourSurMapGui(terrain, x, y);
                    mettreAJourAffichage();

                    JOptionPane.showMessageDialog(this, "Tour améliorée avec succès !", "Amélioration de la tour", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Pas assez d'argent pour améliorer la tour !", "Erreur d'amélioration", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Niveau maximum d'amélioration atteint pour cette tour !", "Erreur d'amélioration", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "La case sélectionnée ne contient pas de tour à améliorer.", "Erreur d'amélioration", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public void mettreAJourAffichage() {
        lifeLabel.setText("Vie: " + jeu.getJoueur().getVies());
        moneyLabel.setText("Argent: " + jeu.getJoueur().getArgent());
        scoreLabel.setText("Score: " + jeu.getJoueur().getScore());
        waveLabel.setText("Vague: " + jeu.getWaveManager().getCurrentWaveNumber());


        
        if (jeu.gameOver()) {
            this.afficherGameOver();
        } else {
        SwingUtilities.invokeLater(() -> {
            terrainPanel.repaint();
        });
        }
    }


    //////////////////////////////
    //  Affichage du Game Over  //
    //////////////////////////////
    
    public void afficherGameOver() {

        JPanel gameOverPanel = new JPanel();

        JLabel backgroundLabel = createBackgroundLabel();
        setContentPane(backgroundLabel);
        
        gameOverPanel.setOpaque(false);
        backgroundLabel.add(gameOverPanel);

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48)); // Utilisez une police stylée
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setForeground(new Color(139,0,0));

        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        gameOverPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    
        gameOverPanel.add(Box.createVerticalGlue());

        JButton rejouerButton = createStyledButton("Rejouer", e -> {
            jeu.resetGame(); 
            jeu.lancerPartie(); 
            dispose();
        });
    
        JButton quitterButton = createStyledButton("Quitter", e -> {
            System.exit(0);
        });

        gameOverPanel.add(gameOverLabel);
        gameOverPanel.add(Box.createVerticalStrut(20));
        gameOverPanel.add(rejouerButton);
        gameOverPanel.add(Box.createVerticalStrut(10));
        gameOverPanel.add(quitterButton);
    
        gameOverPanel.add(Box.createVerticalGlue());

        getContentPane().removeAll(); 
        getContentPane().add(gameOverPanel); 
        revalidate(); 
        repaint(); 
    }


    ////////////////////////////////
    //  Affichage de la Victoire  //
    ////////////////////////////////

    public void afficherVictoire() {

        JPanel victoryPanel = new JPanel();

        JLabel backgroundLabel = createBackgroundLabel();
        setContentPane(backgroundLabel);
        
        victoryPanel.setOpaque(false); 
        backgroundLabel.add(victoryPanel);

        JLabel victoryLabel = new JLabel("Victoire");
        victoryLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48)); 
        victoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        victoryLabel.setForeground(new Color(199,21,133));

        victoryPanel.setLayout(new BoxLayout(victoryPanel, BoxLayout.Y_AXIS));
        victoryPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    
        victoryPanel.add(Box.createVerticalGlue());

        JButton rejouerButton = createStyledButton("Rejouer", e -> {
            jeu.resetGame(); 
            jeu.lancerPartie(); 
            dispose();
        });
    
        JButton quitterButton = createStyledButton("Quitter", e -> {
            System.exit(0); 
        });

        victoryPanel.add(victoryLabel);
        victoryPanel.add(Box.createVerticalStrut(20));
        victoryPanel.add(rejouerButton);
        victoryPanel.add(Box.createVerticalStrut(10));
        victoryPanel.add(quitterButton);
    
        victoryPanel.add(Box.createVerticalGlue());

        getContentPane().removeAll();
        getContentPane().add(victoryPanel); 
        revalidate();
        repaint();
    }


    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setForeground(Color.black);

        int topPadding = 5;
        int leftPadding = 5;
        int bottomPadding = 5;
        int rightPadding = 5;

        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(128, 0, 255), 3, true),
                new EmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding)
        ));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(actionListener);

        return button;
    }


    private JLabel createBackgroundLabel() {
        ImageIcon backgroundImage = new ImageIcon("bin/ressources/sprites/sucrotopia.gif");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        return backgroundLabel;
    }

}
