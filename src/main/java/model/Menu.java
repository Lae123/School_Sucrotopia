package main.java.model;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private static String difficultyLevel;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    // Affiche le menu du jeu pour commencer une partie ou quitter le jeu
    public void afficherMenuPrincipal() {
        int choix = 0;
        boolean saisieValide = false;
    
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Commencer une nouvelle partie");
        System.out.println("2. Quitter");
    
        while (!saisieValide) {
            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
                if (choix == 1 || choix == 2) {
                    saisieValide = true;
                } else {
                    System.out.println("Veuillez choisir un numéro entre 1 et 2.");
                }
            } else {
                System.out.println("Veuillez saisir un nombre valide.");
                scanner.nextLine();
            }
        }
    
        switch (choix) {
            case 1:
                parametrageJeu();
                break;
            case 2:
                System.out.println("Au revoir !");
                System.exit(0);
                break;
            default:
                System.out.println("Choix non valide !");
                break;
        }
    }
    
    
    // Affiche le paramétrage du jeu pour choisir la difficulté du jeu ou le mode Marathon
    // puis, met à jour la variable isMarathon() de Jeu
    public void parametrageJeu() {
        int choixDifficulte = 0;
        boolean saisieValide = false;
    
        System.out.println("=== Paramétrage du Jeu ===");
        System.out.println("Choisissez le niveau de difficulté :");
        System.out.println("1. Facile");
        System.out.println("2. Moyen");
        System.out.println("3. Difficile");
        System.out.println("4. Mode Marathon");
    
        while (!saisieValide) {
            if (scanner.hasNextInt()) {
                choixDifficulte = scanner.nextInt();
                if (choixDifficulte >= 1 && choixDifficulte <= 4) {
                    saisieValide = true;
                } else {
                    System.out.println("Veuillez choisir un numéro entre 1 et 4.");
                }
            } else {
                System.out.println("Veuillez saisir un nombre valide.");
                scanner.nextLine(); 
            }
        }
    
        switch (choixDifficulte) {
            case 1:
                difficultyLevel = "facile";
                Jeu.startGame(difficultyLevel, false);
                break;
            case 2:
                difficultyLevel = "moyen";
                Jeu.startGame(difficultyLevel, false);
                break;
            case 3:
                difficultyLevel = "difficile";
                Jeu.startGame(difficultyLevel, false);
                break;
            case 4:
                difficultyLevel = "marathon";
                Jeu.startGame(difficultyLevel, true);
                break;
            default:
                System.out.println("Choix non valide !");
                break;
        }
    }

    public static String getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void fermerScanner() {
        scanner.close();
    }


    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.afficherMenuPrincipal();

    }
}
