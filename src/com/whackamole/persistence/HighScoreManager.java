package com.whackamole.persistence;

import com.whackamole.exceptions.HighScoreException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Manages serialization/deserialization of high score list to disk.

public class HighScoreManager {
    private static final String FILE = "scores.dat";

    public void saveScores(List<PlayerScore> scores) throws HighScoreException {
        System.out.println(">>> saveScores() CALLED");
        System.out.println(">>> Writing to: " + new java.io.File("scores.dat").getAbsolutePath());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(new ArrayList<>(scores));
        } catch (IOException e) {
            System.out.println(">>> ERROR in saveScores(): " + e.getMessage());
            e.printStackTrace();
            throw new HighScoreException("Failed to save scores", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<PlayerScore> loadScores() throws HighScoreException {
        File f = new File(FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            return (List<PlayerScore>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new HighScoreException("Failed to load scores", e);
        }
    }
}
