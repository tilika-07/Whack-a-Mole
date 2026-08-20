package com.whackamole.model;

import com.whackamole.ui.ImageUtils;
import javax.swing.*;

public class Bomb extends HoleOccupant {
    public Bomb(int lifespan) { super(lifespan); }

    @Override
    public int whack() {
        hide();
        return -500;
    }

    @Override
    public ImageIcon getIcon() {
        return ImageUtils.loadAndScale("/images/bomb.png", 100, 100);
    }
}
