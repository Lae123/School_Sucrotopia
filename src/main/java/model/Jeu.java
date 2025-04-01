package main.java.model;

import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import main.java.GUI.TerrainGui;

import java.io.File;
import java.lang.Thread;
import java.util.Scanner;


public class Jeu implements WaveManager.WaveCallback {
    private Timer timer;
    private long startTime;
    private Joueur joueur;
    private Terrain terrain;
    private static boolean gameRunning;
    private WaveManager waveManager;
    private boolean isMarathonMode;
    private TerrainGui terrainGui;
    private Timer refreshTimer;
    private String niveaudeDifficulte;
    private Clip clip; 
    private boolean soundPlayed = false;
    private static final int REFRESH_INTERVAL = 1000;


    public Jeu(Timer t, long start, Joueur j, Terrain k, String niveauDifficulte, boolean marathon) {
        this.timer = t;
        this.startTime = start;
        this.joueur = j;
        this.terrain = k;
        gameRunning = true;
        this.waveManager = new WaveManager(marathon);
        this.isMarathonMode = marathon;
        this.terrainGui = new TerrainGui(k, this);
        this.niveaudeDifficulte = niveauDifficulte;
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (terrainGui != null) {
                    terrainGui.mettreAJourAffichage();
                }
            }
        }, 0, REFRESH_INTERVAL);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public static void setGameRunning() {
        gameRunning = false;
    }  
    
    public WaveManager getWaveManager() {
        return waveManager;
    }

    public void setMarathonMode(boolean isMarathonMode) { this.isMarathonMode = isMarathonMode; }
    public boolean getMarathon(){ return this.isMarathonMode;}


    // Création d'un thread mettant à jour l'affichage du jeu tant que la variable gameRunning est vraie
    public void lancerPartie() {

        Thread tempsThread = new Thread(() -> {
            try {
                while (gameRunning) {
                    effacerConsole();
                    afficherTempsEcoule();
                    terrain.afficherTerrain();
                    terrain.afficherDetailsJoueur(joueur);
                    joueur.demanderAction(terrain, terrainGui);
                    terrainGui.mettreAJourAffichage();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        tempsThread.start();

    
    // Création d'un timer générant les vagues d'ennemis à intervalles réguliers en fonction de la difficulté choisie :
    // détermine le nombre de vagues et la difficulté de la vague, puis crée et ajoute une nouvelle vague au gestionnaire de vagues 
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                
                if (gameOver()) {
                    setGameRunning();
                    terrainGui.afficherGameOver();
                    finDePartie();
                }

                if (win()) {
                    setGameRunning();
                    terrainGui.afficherVictoire();
                    finDePartie();
                }

                if (gameRunning) {
                    int nbDeVague = determineNombreVague();
                    int difficulteVague = determineDifficulteVague();
                    waveManager.createAndAddWave(niveaudeDifficulte,terrain, difficulteVague, nbDeVague, joueur);
                    onWaveStart();
                }

            }
        }, 10000, 10000);
        

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameRunning = false;
                waveManager.getCurrentWave().stopSchedulers();
                timer.cancel();
                joueur.closeScanner();
                refreshTimer.cancel();
            }
        }, 100000);
    }

    // Vérifie si le gestionnaire de vagues (waveManager) a suffisamment de temps pour démarrer la prochaine vague
    @Override
    public void onWaveStart() {
        if (waveManager.getEnoughTime()) {
            waveManager.startNextWave();
        }
    }

    private int determineDifficulteVague() {
        switch (niveaudeDifficulte.toLowerCase()) {
            case "facile":
                return 5;
            case "moyen":
                return 8;
            case "difficile":
                return 15;
            case "marathon":
                return 10;
            default:
                return 5; 
        }
    }

    private int determineNombreVague() {
        switch (niveaudeDifficulte.toLowerCase()) {
            case "facile":
                return 3;
            case "moyen":
                return 5;
            case "difficile":
                return 10;
            default:
                return 3; 
        }
    }

    // Met à jour l'affichage du jeu dans le terminal en effaçant le contenu de la console
    private void effacerConsole() {
        System.out.print("\033c");
        System.out.flush();
    }

    private void afficherTempsEcoule() {
        long tempsActuel = System.currentTimeMillis();
        long tempsEcoule = (tempsActuel - startTime) / 1000;

        long minutes = tempsEcoule / 60;
        long secondes = tempsEcoule % 60;

        System.out.print("\r");

        System.out.printf("• Temps écoulé • %02d min : %02d sec", minutes, secondes);

        System.out.print("\n");

        System.out.flush();
    }


    // Vérifie si toutes les vagues ont été générées et ont une liste d'ennemis vide
    public boolean win() {
        boolean isWin =  ((waveManager.getCurrentWaveNumber() == (determineNombreVague() - 1)) 
            || waveManager.getCurrentWaveNumber() == determineNombreVague())
            && waveManager.getVague().stream().allMatch(wave -> wave.getEnemies().isEmpty());

        if (isWin && !soundPlayed) {
            jouerSon("bin/ressources/sons/won.wav");
            soundPlayed = true; 
        }
    
        return isWin;
    }
    
    public boolean gameOver() {
        boolean isGameOver =  joueur.getVies() <= 0;

        if (isGameOver && !soundPlayed) {
            jouerSon("bin/ressources/sons/gameOver.wav");
            soundPlayed = true; 
        }

        return isGameOver;

        
    }
    

    // Initialisation des attributs de la partie
    public static void startGame(String niveauDifficulte, boolean m) {
        Timer timer = new Timer();
        Joueur joueur = new Joueur(niveauDifficulte);
        Terrain terrain = new Terrain(11, 11);
        Jeu jeu = new Jeu(timer, System.currentTimeMillis(), joueur, terrain, niveauDifficulte, m);
        jeu.lancerPartie();
    }


    // Demande au joueur s'il veut rejouer. Si oui, réinitialisation du jeu pour une nouvelle partie. Sinon, quiiter le jeu
    private void finDePartie() {
        System.out.print("Voulez-vous rejouer? (oui/non): ");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.next().toLowerCase();

        if (choix.equals("oui")) {
            resetGame();
            lancerPartie();
        } else {
            scanner.close();
            System.out.println("Merci d'avoir joué! Au revoir.");
            System.exit(0);
        }
    }

    // Réinitialise les composants pour une nouvelle partie et annule toutes les tâches planifiées
    public void resetGame() {
        if (timer != null) {
            timer.cancel();
        }
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
    
        joueur = new Joueur(niveaudeDifficulte);
        terrain = new Terrain(11, 11);
        terrainGui = new TerrainGui(terrain, this);
        startTime = System.currentTimeMillis();
        waveManager = new WaveManager(isMarathonMode);
        gameRunning = true;
        timer = new Timer();

        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (terrainGui != null) {
                    terrainGui.mettreAJourAffichage();
                }
            }
        }, 0, REFRESH_INTERVAL);
    }
    
    private void jouerSon(String sonFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sonFilePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
