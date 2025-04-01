package main.java.GUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MenuGui extends JFrame{

    public MenuGui() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sucrotopia");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel backgroundLabel = createBackgroundLabel();
        setContentPane(backgroundLabel);

        JPanel panel = createMainPanel();
        panel.setOpaque(false);
        backgroundLabel.add(panel);

        setVisible(true);
    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    
        panel.add(Box.createVerticalGlue());
    
        JLabel titleLabel = createTitleLabel();
        JButton nouvellePartieButton = createStyledButton("Commencer une nouvelle partie", e -> onNouvellePartieButtonClick());
        JButton quitterButton = createStyledButton("Quitter", this::onQuitterButtonClick);
    
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(nouvellePartieButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(quitterButton);
    
        panel.add(Box.createVerticalGlue());
    
        return panel;
    }
    

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Sucrotopia");
        titleLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(128, 0, 255)); 
        
    
        return titleLabel;
    }

    private JLabel createBackgroundLabel() {
        ImageIcon backgroundImage = new ImageIcon("bin/ressources/sprites/sucrotopia.gif");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        return backgroundLabel;
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

    private void onNouvellePartieButtonClick() {
        dispose(); 
        new StoryTelling(this).setVisible(true); 
    }
    

    private void onQuitterButtonClick(ActionEvent e) {
        System.exit(0);
    }
    

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        MenuGui menuGUI = new MenuGui();
        menuGUI.setVisible(true);
    }
    
}
