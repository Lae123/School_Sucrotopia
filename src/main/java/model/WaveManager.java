package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class WaveManager  {
    List<Vague> vague;
    private int index;
    private long lastVagueStartTime;
    private boolean isMarathonMode;
    private Terrain terrain;

    public WaveManager(boolean marathon) {
        this.vague = new ArrayList<>();
        this.index = 0;
        this.lastVagueStartTime = 0;
        this.isMarathonMode = marathon;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void addWave(Vague wave) {
        vague.add(wave);
    }

    public int getCurrentWaveNumber() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public boolean getEnoughTime() {
        return enoughTimePassed();
    }

    public List<Vague> getVague() {
        return vague;
    }

    // Renvoie la vague actuelle du jeu en se basant sur l'index courant
    public Vague getCurrentWave() {
        if (!vague.isEmpty()) {
            int currentIndex = Math.min(index, vague.size() - 1);
            return vague.get(currentIndex);
        } else {
            return null;
        }
    }

    // Retourne vrai si tous les ennemis dans toutes les vagues ont été éliminés, sinon faux
    public boolean tousEnnemisElimines() {
        for (Vague currentWave : vague) {
            List<Ennemi> ennemis = currentWave.getEnemies();
            for (Ennemi enemy : ennemis) {
                if (enemy.getPointsDeVie() > 0) {
                    return false;
                }
            }
        }
        return true;
    }
    

    // Crée une nouvelle vague d'ennemis en fonction du niveau de difficulté, du nombre d'ennemis et
    // du nombre de vagues, et l'ajoute à la liste des vagues
    public void createAndAddWave(String niveau, Terrain terrain, int numberOfEnemies, int nbDeVague, Joueur joueur) {
        if (isMarathonMode){
            Vague newWave = new Vague(terrain);
            newWave.generateAndMoveEnemiesSequentially(numberOfEnemies, niveau, joueur);
            addWave(newWave);
        }else{
            if (vague.size() < nbDeVague) {
                Vague newWave = new Vague(terrain);
                newWave.generateAndMoveEnemiesSequentially(numberOfEnemies,niveau, joueur);
                addWave(newWave);
            }else{
                System.out.println("Nombre maximum de vagues atteint. Impossible de créer plus de vagues.");
            }
        }
    }

    // Démarre la vague suivante en vérifiant si suffisamment de temps s'est écoulé depuis le début de la dernière vague.
    // Si oui, la vague suivante est générée toutes les 10 secondes et toutes les 5 secondes en mode Marathon
    public void startNextWave() {
        if (isMarathonMode) {
            if (enoughTimePassed()) {
                lastVagueStartTime = System.currentTimeMillis();
                index++;
            } else {
                System.out.println("La prochaine vague ne démarre pas. Pas assez de temps écoulé.");
            }
        } else {
            Vague currentWave = getCurrentWave();
            if (currentWave != null && enoughTimePassed()) {
                lastVagueStartTime = System.currentTimeMillis();
                index++;
            } else {
                System.out.println("La prochaine vague ne démarre pas. Soit toutes les vagues sont terminées, soit pas assez de temps écoulé.");
            }
        }
    }


    // Vérifie si suffisamment de temps s'est écoulé depuis le début de la dernière vague
    private boolean enoughTimePassed() {
        long currentTime = System.currentTimeMillis();
        long elapsedTimeSinceLastWave = currentTime - lastVagueStartTime;
        long tenSecondsInMillis;

        if (isMarathonMode){
            tenSecondsInMillis = 5 * 1000;
        }else{
            tenSecondsInMillis = 10 * 1000;
        }

        boolean enoughTime = elapsedTimeSinceLastWave >= tenSecondsInMillis;

        return enoughTime;
    }

    // Callback interface
    public interface WaveCallback {
        void onWaveStart();
    }

}
