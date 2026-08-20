package com.whackamole.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

// Utility to load and scale icons from classpath resources, with a fallback to filesystem (useful while developing).

public class ImageUtils {
    
    public static ImageIcon loadAndScale(String resourcePath, int width, int height) {
        Image img = null;

        // Try classpath resource first
        try {
            URL url = ImageUtils.class.getResource(resourcePath);
            if (url != null) {
                img = ImageIO.read(url);
            }
        } catch (IOException e) {
            // ignore and fallback below
        }

        
        if (img == null) {
            try {
                String filename = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
                File f = new File("/mnt/data/" + filename);
                if (f.exists()) {
                    img = ImageIO.read(f);
                }
            } catch (IOException ignored) {}
        }

        // Final safety: return an empty icon when nothing loads
        if (img == null) {
            return new ImageIcon();
        }

        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
