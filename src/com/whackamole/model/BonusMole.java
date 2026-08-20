package com.whackamole.model;

import com.whackamole.ui.ImageUtils;
import javax.swing.*;

public class BonusMole extends HoleOccupant {
    public BonusMole(int lifespan) { super(lifespan); }

    @Override
    public int whack() {
        hide();
        return 1000;
    }

    @Override
    public ImageIcon getIcon() {
        return ImageUtils.loadAndScale("/images/bonus.png", 100, 100);
    }
}
