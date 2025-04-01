package main.java.GUI;

import main.java.model.Jeu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DifficulteGui extends JFrame {

    private MenuGui parentMenu;

    public DifficulteGui(MenuGui parentMenu) {
        this.parentMenu = parentMenu;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sucrotopia - Choisir le niveau de difficulté");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel backgroundLabel = createBackgroundLabel();
        setContentPane(backgroundLabel);

        JPanel panel = createPanel();
        addComponentsToPanel(panel);

        backgroundLabel.add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        panel.setOpaque(false);
        return panel;
    }

    private void addComponentsToPanel(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
    
        JLabel label = createLabel("Choisissez votre défi");
        JButton facileButton = createStyledButton("Facile", e -> onDifficulteSelected("facile"));
        JButton moyenButton = createStyledButton("Moyen", e -> onDifficulteSelected("moyen"));
        JButton marathonButton = createStyledButton("Marathon", e -> onDifficulteSelected("marathon"));
        JButton difficileButton = createStyledButton("Difficile", e -> onDifficulteSelected("difficile"));
        
        JButton retourButton = createStyledButton("Retour", e -> onRetourButtonClick());
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(label, gbc);
    
        gbc.gridy = 1;
        panel.add(facileButton, gbc);
    
        gbc.gridy = 2;
        panel.add(moyenButton, gbc);
    
        gbc.gridy = 3;
        panel.add(difficileButton, gbc);
    
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(marathonButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(retourButton, gbc);
    }

    private void onRetourButtonClick() {
        dispose();
        parentMenu.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(new Color(128, 0, 255));

        return label;
    }

    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setForeground(new Color(128, 0, 255));
        button.setBackground(new Color(34, 167, 240));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(176, 113, 201), 3, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(actionListener);

        button.addMouseListener(new DepthEffectMouseListener(button));

        return button;
    }

    private void onDifficulteSelected(String difficulte) {
        dispose();
        Jeu.startGame(difficulte, "marathon".equals(difficulte));
    }

    private JLabel createBackgroundLabel() {
        ImageIcon backgroundImage = new ImageIcon("bin/ressources/sprites/sucrotopia.gif");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        return backgroundLabel;
    }


    // Applique des effets visuels de profondeur lors des interactions de la souris avec un bouton

    class DepthEffectMouseListener implements MouseListener {
        private JButton button;

        public DepthEffectMouseListener(JButton button) {
            this.button = button;
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            button.setBackground(new Color(176, 224, 230));
            button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(70, 130, 180), 3, true),
                    new EmptyBorder(7, 17, 7, 17)
            ));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            button.setBackground(new Color(34, 167, 240));
            button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(176, 113, 201), 3, true),
                    new EmptyBorder(10, 20, 10, 20)
            ));
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    
}
