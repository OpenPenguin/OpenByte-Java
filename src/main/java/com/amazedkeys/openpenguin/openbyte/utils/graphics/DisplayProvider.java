package com.amazedkeys.openpenguin.openbyte.utils.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayProvider extends JFrame {
    private PixelRasterView pixelView;
    private JPanel rasterWindow;

    public DisplayProvider(PixelRasterView pixelView) {
        this.pixelView = pixelView;

        this.rasterWindow = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage img = pixelView.getRaster();
                Image scaledImage = img.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_FAST);
                g.drawImage(scaledImage, 0, 0, null);
                System.out.println("RASTER WINDOW PAINTED");
            }

            public void forceUpdate() {
                removeAll();
                this.paintComponents(getGraphics());
                revalidate();
                repaint();
            }
        };

        this.add(this.rasterWindow);

        int width = 1000;
        int height = 312;

        this.setSize(width, height);
        this.pack();
        this.setSize(width, height);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // this.rasterWindow.paintComponents(g);
        System.out.println("MASTER PAINTED!!");
    }

    public void updateRaster() {
        System.out.println("Redrawing!");

        revalidate();
        repaint();

    }

}
