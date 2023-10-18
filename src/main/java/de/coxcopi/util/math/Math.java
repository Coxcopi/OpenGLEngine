package de.coxcopi.util.math;

/**
 * Extends Javas built-in Math class by a number of various helper functions.
 */
public class Math {

    /**
     * Linearly interpolates between to values by a given factor.
     * @param a The start value.
     * @param b The end value.
     * @param t The interpolation factor.
     * @return An interpolated value between a and b by factor t.
     */
    public static double lerp(double a, double b, double t) {
        return (a + (b - a) * t);
    }

    public static double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * Converts a rotation from radians to degrees.
     * @param angle The angle in radians.
     * @return The angle in degrees.
     */
    public static double radToDeg(double angle) {
        return angle * (180.0 / java.lang.Math.PI);
    }

    /**
     * Converts a rotation from degrees to radians.
     * @param angle The angle in degrees.
     * @return The angle in radians.
     */
    public static double degToRad(double angle) {
        return angle * (java.lang.Math.PI / 180.0);
    }

}
