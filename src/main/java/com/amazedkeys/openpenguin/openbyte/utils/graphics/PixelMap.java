package com.amazedkeys.openpenguin.openbyte.utils.graphics;

import com.amazedkeys.openpenguin.openbyte.utils.types.RGB;
import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

import java.awt.image.BufferedImage;

public class PixelMap {
    protected final Vector2 resolution;
    protected final int colorDepth;
    protected final long pixelCount;
    protected RGB[][] pixelMaps;

    private void generatePixelMap() {
        int width = (int) this.resolution.getX();
        int height = (int) this.resolution.getY();
        this.pixelMaps = new RGB[width][height];
    }

    public PixelMap(Vector2 resolution, int colorDepth) {
        this.resolution = resolution;
        this.colorDepth = colorDepth;

        this.pixelCount = ((int) this.resolution.getX()) * ((int) this.resolution.getY());

        this.generatePixelMap();
    }

    public void setRGB(int x, int y, RGB value) {
        this.pixelMaps[x][y] = value;
    }

    public RGB getRGB(int x, int y) {
        return this.pixelMaps[x][y];
    }

    public int getRGBInt(int x, int y) {
        return RGB.RGBToInt(this.pixelMaps[x][y]);
    }
}
