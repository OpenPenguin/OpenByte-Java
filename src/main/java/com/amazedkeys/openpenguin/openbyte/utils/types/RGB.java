package com.amazedkeys.openpenguin.openbyte.utils.types;

import com.amazedkeys.openpenguin.openbyte.utils.shared.Maths;

public class RGB {
    // Attributes
    private int R,G,B;

    // Constructors
    public RGB(int R, int G, int B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public RGB(int intValue) {
        // TODO
    }

    // Getters and Setters
    public int getR() {
        return R;
    }
    public void setR(int r) {
        R = r;
    }
    public int getG() {
        return G;
    }
    public void setG(int g) {
        G = g;
    }
    public int getB() {
        return B;
    }
    public void setB(int b) {
        B = b;
    }

    // Static Wrappers
    public int asInt() {
        return RGBToInt(this);
    }

    // Static Methods
    public static int RGBToInt(int r, int g, int b) {
        int cr = Maths.clamp(255, 0, r);
        int cg = Maths.clamp(255, 0, g);
        int cb = Maths.clamp(255, 0, b);
        return (cr << 16) | (cg << 8) | cb;
    }
    public static int RGBToInt(RGB in) {
        int cr = Maths.clamp(255, 0, in.R);
        int cg = Maths.clamp(255, 0, in.G);
        int cb = Maths.clamp(255, 0, in.B);
        return (cr << 16) | (cg << 8) | cb;
    }
    public static RGB IntToRGB(int value) {
        int r = (value >> 16)   & 0xFF;
        int g = (value >> 8)    & 0xFF;
        int b = value           & 0xFF;
        return new RGB(r, g, b);
    }
}
