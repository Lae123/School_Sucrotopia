package main.java.GUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class StoryTelling extends JFrame {

    private JTextArea storyTextArea;
    private int currentIndex = 0;
    private Timer timer;
    private Clip clip; 
    private String storyText = "Dans un monde lointain, le royaume enchanté de Sucrotopia, peuplé de délices sucrés et de bonbons délicieux, est actuellement en péril ! \n Des bonbons maléfiques et infectés cherchent à envahir le royaume ! \n Votre mission est de défendre le royaume et d'empêcher cette invasion sucrée ! ";

    public StoryTelling(MenuGui parent) {
        initializeUI(parent);
        jouerSon();
    }

    private void initializeUI(MenuGui parent) {
        setTitle("Storytelling - Sucrotopia");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(parent);

        storyTextArea = new JTextArea();
        storyTextArea.setBackground(Color.BLACK);
        storyTextArea.setForeground(Color.WHITE);
        storyTextArea.setFont(new Font("Sans", Font.PLAIN, 20));
        storyTextArea.setLineWrap(true);
        storyTextArea.setWrapStyleWord(true);
        storyTextArea.setEditable(false);

        JButton continuerButton = new JButton("Skip");
        continuerButton.addActionListener(e -> onNouvellePartieButtonClick(parent));

        Dimension buttonSize = new Dimension(10, 10);
        continuerButton.setPreferredSize(buttonSize);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Ajoutez un remplissage supplémentaire en haut, à gauche, à droite et en bas
        gbc.insets = new Insets(50, 50, 50, 50);

        mainPanel.add(storyTextArea, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.2;

        mainPanel.add(continuerButton, gbc);

        setContentPane(mainPanel);

        // Ajoutez un WindowListener pour détecter la fermeture de la fenêtre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                arreterSon(); // Appeler la méthode pour arrêter le son
            }
        });

        // Initialisez et démarrez le timer pour afficher le texte lettre par lettre.
        timer = new Timer(18, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < storyText.length()) {
                    storyTextArea.append(String.valueOf(storyText.charAt(currentIndex)));
                    currentIndex++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();

        setVisible(true);
    }

    private void onNouvellePartieButtonClick(MenuGui p) {
        arreterSon();
        dispose();
        new DifficulteGui(p).setVisible(true);
    }

    private void jouerSon() {
        try {
            String sonFilePath = "bin/ressources/sons/start.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sonFilePath).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Jouer le son
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Arrêter le son et fermer le Clip
    private void arreterSon() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
