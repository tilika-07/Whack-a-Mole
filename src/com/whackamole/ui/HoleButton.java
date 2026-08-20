package com.whackamole.ui;

import com.whackamole.model.HoleOccupant;

import javax.swing.*;
import java.awt.*;

// Represents a cell/hole in the grid.

public class HoleButton extends JButton {
    private final int index;
    private HoleOccupant occupant;
    private final GameBoard board;

    public HoleButton(int index, GameBoard board) {
        this.index = index;
        this.board = board;
        setPreferredSize(new Dimension(120, 100));

        
        setIcon(ImageUtils.loadAndScale("/images/hole.png", 120, 100));

        addActionListener(e -> board.handleWhack(index));
    }

    public synchronized void setOccupant(HoleOccupant occ) {
        this.occupant = occ;
        if (occ == null) {
            
            setIcon(ImageUtils.loadAndScale("/images/hole.png", 120, 100));
        } else {
            setIcon(occ.getIcon());
        }
    }

    public synchronized HoleOccupant getOccupant() {
        return occupant;
    }

    public synchronized void clearOccupant() {
        occupant = null;
        
        setIcon(ImageUtils.loadAndScale("/images/hole.png", 120, 100));
    }
}
