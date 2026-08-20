package com.whackamole.ui;

import com.whackamole.engine.GameEngine;
import com.whackamole.model.HoleOccupant;
import com.whackamole.persistence.HighScoreManager;
import com.whackamole.persistence.PlayerScore;
import com.whackamole.exceptions.HighScoreException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

//Main JFrame containing score, time and a grid of HoleButtons.

public class GameBoard extends JFrame {
    public static final int ROWS = 3;
    public static final int COLS = 5;
    private final HoleButton[] holes = new HoleButton[ROWS * COLS];
    private final JLabel scoreLabel = new JLabel("Score: 0");
    private final JLabel timeLabel = new JLabel("Time: 0s");
    private Thread gameThread;
    private GameEngine engine;
    private int score = 0;

    private final HighScoreManager highScoreManager = new HighScoreManager();

    public GameBoard() {
        super("Whack-A-Mole");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        buildUI();
        tryLoadScores();
        startEngine();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Graceful shutdown: interrupt engine thread
                if (gameThread != null) {
                    gameThread.interrupt();
                    try {
                        gameThread.join(1000);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                }
            }
        });
    }

    private void buildUI() {
        JPanel top = new JPanel(new BorderLayout());
        top.add(scoreLabel, BorderLayout.WEST);
        top.add(timeLabel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(ROWS, COLS, 10, 10));
        for (int i = 0; i < holes.length; i++) {
            HoleButton hb = new HoleButton(i, this);
            holes[i] = hb;
            grid.add(hb);
        }
        add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton start = new JButton("Restart");
        start.addActionListener(e -> restartGame());
        bottom.add(start);
        add(bottom, BorderLayout.SOUTH);
    }

    private void tryLoadScores() {
        try {
            List<PlayerScore> scores = highScoreManager.loadScores();
            // Use loaded scores
            System.out.println("Loaded high scores: " + scores.size());
        } catch (HighScoreException e) {
            // Show an alert and continue with empty scores per spec
            JOptionPane.showMessageDialog(this,
                    "Could not load high scores: " + e.getMessage(),
                    "High Score Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void startEngine() {
        engine = new GameEngine(this, holes);
        gameThread = new Thread(engine, "GameEngine");
        gameThread.start();
    }

    public synchronized void addScore(int delta) {
        score += delta;
        SwingUtilities.invokeLater(() -> scoreLabel.setText("Score: " + score));
    }

    public synchronized int getScore() { return score; }

    public void setTimeRemaining(int seconds) {
        SwingUtilities.invokeLater(() -> timeLabel.setText("Time: " + seconds + "s"));
    }

    public void restartGame() {
        // stop old thread
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
            try { gameThread.join(500); } catch (InterruptedException ignored) {}
        }
        // reset UI and start new engine
        score = 0;
        scoreLabel.setText("Score: 0");
        for (HoleButton hb : holes) hb.clearOccupant();
        startEngine();
    }

    // Called by HoleButton when clicked: whack occupant at index
    public void handleWhack(int index) {
        HoleButton hb = holes[index];
        HoleOccupant occ = hb.getOccupant();
        if (occ == null) return;
        int delta = occ.whack(); // polymorphic call
        addScore(delta);
        hb.clearOccupant(); // hide after whack
    }
}
