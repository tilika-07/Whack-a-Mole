package com.whackamole.model;

import javax.swing.*;
import java.io.Serializable;

// Abstract base class for any object that can appear from a hole.

public abstract class HoleOccupant implements Serializable {
    private static final long serialVersionUID = 1L;
    protected boolean visible = true;
    protected int lifespan; // number of ticks remaining

    public HoleOccupant(int lifespan) {
        this.lifespan = lifespan;
    }

    // Called every engine tick to decrement lifespan
    public void tick() {
        if (lifespan > 0) lifespan--;
        if (lifespan == 0) visible = false;
    }
    public boolean isVisible() { return visible; }
    public void hide() { visible = false; }

    // abstract contract
    public abstract int whack();
    public abstract ImageIcon getIcon();
}
