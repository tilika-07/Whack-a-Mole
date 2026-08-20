package com.whackamole.engine;

import com.whackamole.ui.HoleButton;
import com.whackamole.ui.GameBoard;
import com.whackamole.model.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine implements Runnable {       //game loop is executed on separate thread. 
    private final GameBoard board;
    private final HoleButton[] holes;
    private volatile boolean running = true;
    private final Random rand = new Random();
    private int timeRemaining = 30;        

    public GameEngine(GameBoard board, HoleButton[] holes) {
        this.board = board;
        this.holes = holes;
    }

    @Override
    public void run() {
        try {
            while (running && timeRemaining > 0) {
                // update UI time
                SwingUtilities.invokeLater(() -> board.setTimeRemaining(timeRemaining));

                // tick occupants: hide after lifespan
                for (HoleButton hb : holes) {
                    // get occupant reference
                    final var occ = hb.getOccupant();
                    if (occ != null) {
                        occ.tick();
                        if (!occ.isVisible()) {
                            // UI update must be on EDT
                            SwingUtilities.invokeLater(hb::clearOccupant);
                        }
                    }
                }

                //spawn logic
                        List<Integer> emptyIndices = new ArrayList<>();
                        for (int i = 0; i < holes.length; i++) {
                            if (holes[i].getOccupant() == null) emptyIndices.add(i);
                        }

                        if (!emptyIndices.isEmpty()) {
                            int chosen = emptyIndices.get(rand.nextInt(emptyIndices.size()));
                            HoleButton target = holes[chosen];
                            
                            synchronized (target) {
                                if (target.getOccupant() == null) {
                                    HoleOccupant occ = randomOccupant();
                                    SwingUtilities.invokeLater(() -> target.setOccupant(occ));
                                }
                            }
                        }
                        

                Thread.sleep(1000); // pacing
                timeRemaining--;
            }
                SwingUtilities.invokeLater(() -> {
            board.setTimeRemaining(0);

            // clear all holes when game over
            for (HoleButton hb : holes) {
                hb.clearOccupant();
            }
        });
        
try {
    // print working dir 
    System.out.println("WORKING DIR: " + System.getProperty("user.dir"));

    com.whackamole.persistence.HighScoreManager hsm = new com.whackamole.persistence.HighScoreManager();

    // load existing scores (returns empty list if file missing)
    java.util.List<com.whackamole.persistence.PlayerScore> scores = hsm.loadScores();
    System.out.println("Loaded high scores (pre-save): " + scores.size());

    // add the current score
    scores.add(new com.whackamole.persistence.PlayerScore("Player", board.getScore()));

    // sort descending
    scores.sort((a,b) -> Integer.compare(b.getScore(), a.getScore()));
    if (scores.size() > 10) scores = new java.util.ArrayList<>(scores.subList(0,10));

    //create scores.dat if missing
    hsm.saveScores(scores);

    java.io.File f = new java.io.File("scores.dat");
    System.out.println("WROTE: " + f.getAbsolutePath() + " (exists=" + f.exists() + ", len=" + (f.exists() ? f.length() : 0L) + ")");

    // print scores
    System.out.println("Saved high scores:");
    for (int i = 0; i < scores.size(); i++) {
        System.out.printf("%d: %s - %d%n", i+1, scores.get(i).getName(), scores.get(i).getScore());
    }
} catch (com.whackamole.exceptions.HighScoreException ex) {
    System.err.println("HighScore save/load failed: " + ex.getMessage());
    ex.printStackTrace();
}

        } catch (InterruptedException ie) {
            
            running = false;
            
            SwingUtilities.invokeLater(() -> board.setTimeRemaining(0));
            
        } catch (RuntimeException rex) {
            // Allow UI to show an error if InvalidGameStateException occurred
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Fatal game error: " + rex.getMessage()));
            running = false;
        } finally {
            // final cleanup
        }

    }

    private HoleOccupant randomOccupant() {
        int r = rand.nextInt(10);
        if (r < 7) return new Mole(3);         // common
        if (r < 9) return new BonusMole(2);   // rarer
        return new Bomb(2);                   // rare
    }
}
