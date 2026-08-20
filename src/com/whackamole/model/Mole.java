package com.whackamole.model;

import com.whackamole.ui.ImageUtils;
import javax.swing.*;

public class Mole extends HoleOccupant {
    public Mole(int lifespan) { super(lifespan); }

    @Override
    public int whack() {
        hide();
        return 100;
    }

    @Override
    public ImageIcon getIcon() {
        // Slightly smaller than hole background to give padding
        return ImageUtils.loadAndScale("/images/mole.png", 100, 100);
    }
}
