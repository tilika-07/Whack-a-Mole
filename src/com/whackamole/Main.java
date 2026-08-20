package com.whackamole;

import javax.swing.*;
import com.whackamole.ui.GameBoard;


public class Main {
    public static void main(String[] args) {
        // Launch Swing UI on EDT
        SwingUtilities.invokeLater(() -> {
            GameBoard board = new GameBoard();
            board.setVisible(true);
        });
    }
}