package de.coxcopi.util;

import de.coxcopi.util.math.Vector3;

public class Color {

    public static final Color WHITE = new Color(1.0, 1.0, 1.0);
    public static final Color BLACK = new Color(0.0, 0.0, 0.0, 1.0);
    public static final Color RED = new Color(1.0, 0.0, 0.0);
    public static final Color GREEN = new Color(0.0, 1.0, 0.0);
    public static final Color BLUE = new Color(0.0, 0.0, 1.0);

    public double r;
    public double g;
    public double b;
    public double a;

    public Color() {
        r = 1.0;
        g = 1.0;
        b = 1.0;
        a = 1.0;
    }

    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0;
    }

    public Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(double rgb) {
        this.r = rgb;
        this.g = rgb;
        this.b = rgb;
        this.a = 1.0;
    }

    public Color(double rgb, double a) {
        this.r = rgb;
        this.g = rgb;
        this.b = rgb;
        this.a = a;
    }

    public Color(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }

    public Color(String hex) {
        if (hex.startsWith("#") && hex.length() > 1) {
            hex = hex.substring(1);
        }
        if (hex.length() != 6) {
            return;
        }
        r = ((double) Integer.valueOf(hex.substring(0, 2), 16)) / 255.0f;
        g = ((double) Integer.valueOf(hex.substring(2, 4), 16)) / 255.0f;
        b = ((double) Integer.valueOf(hex.substring(4, 6), 16)) / 255.0f;
    }

    public Color(String hex, double a) {
        final Color hexC = new Color(hex);
        r = hexC.r;
        g = hexC.g;
        b = hexC.b;
        this.a = a;
    }

    public static Color fromHSV() {
        // TODO: Implement
        return new Color();
    }

    public void multiply(double x) {
        r *= x;
        g *= x;
        b *= x;
        a *= x;
    }

    public Color multiplied(double x) {
        final Color color = new Color(this);
        color.multiply(x);
        return color;
    }

    public Vector3 toVector3() {
        return new Vector3(r, g, b);
    }

    public float[] toUniformFloatArray() {
        return new float[] {(float) r, (float) g, (float) b, (float) a};
    }

    public float[] toUniformFloatArrayNoAlpha() {
        return new float[] {(float) r, (float) g, (float) b};
    }
}
