package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.java.geometry.RealCoordinates;

public class Vague {
    private List<Ennemi> enemies;
    private Terrain terrain;
    private final ScheduledExecutorService enemyGenerationScheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService enemyMovementScheduler = Executors.newScheduledThreadPool(1);
    private final Object enemiesLock = new Object();
    private static final long timeIntervalBetweenMoves = 1000L; 


    public Vague(Terrain t) {
        this.enemies = new ArrayList<>();
        this.terrain = t;
    }

    public void addEnemy(Ennemi enemy) {
        synchronized (enemiesLock) {
            enemies.add(enemy);
        }
    }

    public void removeEnemy(Ennemi enemy) {
        synchronized (enemiesLock) {
            enemies.remove(enemy);
        }
    }

    public List<Ennemi> getEnemies() {
        return enemies;
    }

    // Génère un type d'ennemi aléatoire en fonction du niveau spécifié et des coordonnées fournies
    public static Ennemi choisirTypeEnnemiAleatoire(String niveau, RealCoordinates coordinates) {
        Random random = new Random();
        int randomIndex = getRandomEnemyIndex(niveau, random);

        switch (randomIndex) {
            case 0:
                return new GateauMoisi(coordinates);
            case 1:
                return new GeleeEnragee(coordinates);
            case 2:
                return new PainDepice(coordinates);
            case 3:
                return new ChocolatMutant(coordinates);
            default:
                return null;
        }
    }

    // Génère un index aléatoire en fonction du niveau et d'une probabilité définie
    private static int getRandomEnemyIndex(String niveau, Random random) {
        double probability;
    
        switch (niveau.toLowerCase()) {
            case "facile":
                probability = Math.min(0.1 * 5, 0.8); 
                break;
            case "moyen":
                probability = Math.min(0.1 * 8, 0.8); 
                break;
            case "difficile":
                probability = Math.min(0.1 * 10, 0.8); 
                break;
            case "marathon":
                probability = Math.min(0.1 * 5, 0.8); 
                break;
            default:
                probability = Math.min(0.1 * 5, 0.8); 
                break;
        }
    
        if (random.nextDouble() < probability) {
            return random.nextInt(4);
        } else {
            return random.nextInt(3);
        }
    }
    

    // Retourne aléatoirement une coordonnée de la première colonne du terrain
    public RealCoordinates getRandomCoordinateFromFirstColumn() {
        RealCoordinates[] coordinatesArray = terrain.getFirstCoor();
        
        if (coordinatesArray.length > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(coordinatesArray.length);
        
            return coordinatesArray[randomIndex];
        } else {
            throw new IllegalStateException("Pas de coordonnées valides.");
        }
    }
    
    // Génère et déplace séquentiellement un certain nombre d'ennemis en fonction de la difficulté spécifiée :
    // Déplace l'ennemi sur le terrain, attaque le mur s'il l'a atteint, et supprime l'ennemi de la vague s'il est éliminé
    public void generateAndMoveEnemiesSequentially(int numberOfEnemies, String niveau, Joueur joueur) {
        ScheduledExecutorService generationScheduler = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService movementScheduler = Executors.newScheduledThreadPool(1);

        for (int i = 0; i < numberOfEnemies; i++) {
            generationScheduler.schedule(() -> {
                RealCoordinates sCoordinates = getRandomCoordinateFromFirstColumn();
                Ennemi ennemi = choisirTypeEnnemiAleatoire(niveau, sCoordinates);
                addEnemy(ennemi);

                // Démarre le mouvement de l'ennemi ajouté après un délai
                movementScheduler.scheduleWithFixedDelay(() -> {
                    synchronized (enemiesLock) {
                        if (!ennemi.murAtteint()) {
                            ennemi.deplacer(terrain);
                        } else if (joueur.getVies() > 0 && !ennemi.estElimine()) {
                            ennemi.attaquer(joueur);
                        }
                
                        if (ennemi.estElimine()) {
                            removeEnemy(ennemi);
                        }
                    }

                }, 0, timeIntervalBetweenMoves, TimeUnit.MILLISECONDS);
            }, i * timeIntervalBetweenMoves, TimeUnit.MILLISECONDS);
        }
    }
    

    // Arrête les planificateurs de génération et de mouvement des ennemis
    public void stopSchedulers() {
        enemyGenerationScheduler.shutdown();
        enemyMovementScheduler.shutdown();
    }

}
