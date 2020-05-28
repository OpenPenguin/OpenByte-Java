package com.amazedkeys.openpenguin.openbyte.utils.graphics;

import com.amazedkeys.openpenguin.openbyte.utils.shared.Maths;
import com.amazedkeys.openpenguin.openbyte.utils.shared.Tiers;
import com.amazedkeys.openpenguin.openbyte.utils.types.RGB;
import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

import java.awt.image.BufferedImage;

public class PixelRasterView {
    private static final int rasterType = BufferedImage.TYPE_INT_RGB;

    private BufferedImage raster;
    protected final Vector2 resolution;
    protected final int colorDepth;

    private void createBufferedImage() {
        int width = (int) this.resolution.getX();
        int height = (int) this.resolution.getY();
        System.out.println("Creating raster with a width of " + width + " and a height of " + height);
        this.raster = new BufferedImage(width, height, rasterType);
    }

    public PixelRasterView(Vector2 resolution, int colorDepth) {
        this.resolution = resolution;
        this.colorDepth = colorDepth;
        this.createBufferedImage();
    }

    public PixelRasterView(Tiers tier) {
        if (tier == Tiers.TIER_TWO) {
            this.resolution = new Vector2(80, 25);
            this.colorDepth = 4;
        } else if (tier == Tiers.TIER_THREE) {
            this.resolution = new Vector2(160, 50);
            this.colorDepth = 8;
        } else {
            this.resolution = new Vector2(50, 16);
            this.colorDepth = 1;
        }
        this.createBufferedImage();
    }

    public void setRGB(int x, int y, int r, int g, int b) {
        int cr = Maths.clamp(255, 0, r);
        int cg = Maths.clamp(255, 0, g);
        int cb = Maths.clamp(255, 0, b);
        int ci = (cr << 16) | (cg << 8) | cb;
        System.out.println("Setting pixel (" + x + "," + y + ") to " + ci + "!");

        if (ci < 0 || ci > 0xFFFFFF) {
            System.out.println("ILLEGAL VALUE!");
        }

        this.raster.setRGB(x, y, ci);
    }

    public void setRGB(int x, int y, RGB value) {
        this.raster.setRGB(x, y, value.asInt());
    }

    public int getRGBAsInt(int x, int y) {
        return this.raster.getRGB(x, y);
    }

    public RGB getRGB(int x, int y) {
        return RGB.IntToRGB(this.raster.getRGB(x, y));
    }

    public Vector2 getResolution() {
        return this.resolution;
    }

    public BufferedImage getRaster() {
        return this.raster;
    }

}
