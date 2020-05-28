package com.amazedkeys.openpenguin.openbyte.utils.types;

import com.amazedkeys.openpenguin.openbyte.utils.shared.Maths;
import com.amazedkeys.openpenguin.openbyte.utils.shared.RoundingTypes;

public class Vector2 {
    // Attributes
    private double x, y, magnitude;

    // Constructors
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
        this.magnitude = 0;
    }

    private Vector2(double x, double y, double magnitude) {
        this.x = x;
        this.y = y;
        this.magnitude = magnitude;
    }

    // Attribute Getters and Setters
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getMagnitude() {
        return magnitude;
    }

    // Rounded Attribute Getters
    public int getXRounded(RoundingTypes mode) {
        return Maths.round(mode, this.x);
    }
    public int getYRounded(RoundingTypes mode) {
        return Maths.round(mode, this.y);
    }

    public int getXRounded() {
        return getXRounded(RoundingTypes.ROUND_AUTO);
    }
    public int getYRounded() {
        return getYRounded(RoundingTypes.ROUND_AUTO);
    }

    // Private Helpers
    private static double pyth(Vector2 a, Vector2 b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        double us = Math.pow(dx, 2) + Math.pow(dy, 2);
        double mg = Math.sqrt(us);
        return mg;
    }

    // Basic Maths (static)
    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2((a.x + b.x), (a.y + b.y));
    }
    public static Vector2 subtract(Vector2 a, Vector2 b) {
        return new Vector2((a.x + b.x), (a.y + b.y), pyth(a, b));
    }
    public static Vector2 multiply(Vector2 a, Vector2 b) {
        return new Vector2((a.x * b.x), (a.y * b.y));
    }
    public static Vector2 divide(Vector2 a, Vector2 b) {
        return new Vector2((a.x / b.x), (a.y / b.y));
    }
}
