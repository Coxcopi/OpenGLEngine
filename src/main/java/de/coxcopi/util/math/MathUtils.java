package de.coxcopi.util.math;

import org.jetbrains.annotations.NotNull;

/**
 * Extends Javas built-in Math class by a number of various helper functions.
 */
public class MathUtils {

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

    /**
     * Maps the given value from a given range in to a given range out.
     * For example, map(x, 0, 1, -1, 1) maps the value x from a range between 0 and 1
     * to a range between -1 and 1.
     * @param x The value to map.
     * @param inMin The minimum value of the input range.
     * @param inMax The maximum value of the input range.
     * @param outMin The minimum value of the output range.
     * @param outMax The maximum value of the output range.
     * @return The mapped value.
     */
    public static double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static double clamp(double x, double min, double max) {
        return Math.min(max, Math.max(x, min));
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

    /**
     * Converts from cartesian to spherical coordinates.
     * @param position The cartesian position (x, y, z).
     * @return The spherical position (r, theta, phi),
     * where r describes the radius, theta describes the azimuth
     * and phi describes the zenith.
     */
    public static Vector3 cartesianToSpherical(@NotNull Vector3 position) {
        assert position.x != 0 && position.y != 0 && position.z != 0;
        final double r = position.length();
        final double theta = Math.atan(position.y / position.x);
        final double phi = Math.atan(Math.sqrt(position.x * position.x + position.y * position.y) / position.z);
        return new Vector3(r, theta, phi);
    }

    /**
     * Converts from spherical to cartesian coordinates.
     * @param position The spherical position (r, theta, phi),
     * where r is the radius, theta is the azimuth and phi is the zenith.
     * @return The cartesian position (x, y, z).
     */
    public static Vector3 sphericalToCartesian(@NotNull Vector3 position) {
        final double r = position.x;
        final double theta = position.y;
        final double phi = position.z;
        return new Vector3(
                r * Math.sin(phi) * Math.cos(theta),
                r * Math.sin(phi) * Math.sin(theta),
                r * Math.cos(phi)
        );
    }
}
