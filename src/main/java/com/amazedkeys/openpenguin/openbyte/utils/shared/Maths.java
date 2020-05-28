package com.amazedkeys.openpenguin.openbyte.utils.shared;

public final class Maths {
    private Maths() {}

    public static int roundUp(double value) {
        double d = value % 1;
        return (int) (value + (1 - d));
    }

    public static int roundDown(double value) {
        double d = value % 1;
        return (int) (value - d);
    }

    public static int round(double value) {
        double d = value % 1;
        if (d >= 0.5) {
            return roundUp(value);
        } else {
            return roundDown(value);
        }
    }

    public static int round(RoundingTypes mode, double value) {
        if (mode == RoundingTypes.ROUND_UP) {
            return roundUp(value);
        } else if (mode == RoundingTypes.ROUND_DOWN) {
            return roundDown(value);
        } else {
            return round(value);
        }
    }

    public static int clamp(int max, int min, int value) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else {
            return value;
        }
    }

    public static double clamp(double max, double min, double value) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else {
            return value;
        }
    }

}
