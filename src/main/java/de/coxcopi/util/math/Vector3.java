package de.coxcopi.util.math;

import org.jetbrains.annotations.NotNull;

/**
 * Three-dimensional vector.
 */
public class Vector3 {

    /**
     * The vector's x component (x3).
     */
    public double x;
    /**
     * The vector's y component (x2).
     */
    public double y;
    /**
     * The vector's z component (x1).
     */
    public double z;

    /**
     * Constructs an empty vector.
     */
    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Constructs a new vector with the specified values.
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Constructs a new vector from a given vector. Useful for
     * duplicating vectors. Returns an exact copy.
     * @param vector The copied vector.
     */
    public Vector3(@NotNull Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    /**
     * Global right vector (positive x).
     * @return The right vector (1, 0, 0).
     */
    public static Vector3 RIGHT() {
        return new Vector3(1, 0, 0);
    }

    /**
     * Global left vector (negative x).
     * @return The right vector (-1, 0, 0).
     */
    public static Vector3 LEFT() {
        return new Vector3(-1, 0, 0);
    }

    /**
     * Global up vector (positive y).
     * @return The up vector (0, 1, 0).
     */
    public static Vector3 UP() {
        return new Vector3(0, 1, 0);
    }

    /**
     * Global down vector (negative y).
     * @return The up vector (0, -1, 0).
     */
    public static Vector3 DOWN() {
        return new Vector3(0, -1, 0);
    }

    /**
     * Global front vector (negative z).
     * @return The front vector (0, 0, -1).
     */
    public static Vector3 FRONT() {
        return new Vector3(0, 0, -1);
    }

    /**
     * Global back vector (positive z).
     * @return The back vector (0, 0, 1).
     */
    public static Vector3 BACK() {
        return new Vector3(0, 0, 1);
    }

    /**
     * Constructs a new vector;
     * @param a The vectors x, y and z value.
     */
    public Vector3(double a) {
        this.x = a;
        this.y = a;
        this.z = a;
    }

    public static Vector3 sphericalToCartesian(double radius, double theta, double phi) {
        final double x = radius * java.lang.Math.cos(theta) * java.lang.Math.cos(phi);
        final double y = -radius * java.lang.Math.sin(theta) * java.lang.Math.sin(phi);
        final double z = radius * java.lang.Math.cos(theta);
        return new Vector3(x, y, z);
    }

    public static Vector3 cartesianToSpherical(Vector3 point) {
        final double radius = point.length();
        final double theta = java.lang.Math.acos(point.y / radius);
        final double phi = java.lang.Math.atan(-point.z / point.x);
        return new Vector3(radius, theta, phi);
    }

    /**
     * Calculates the vector's length.
     * @return The length.
     */
    public double length() {
        return java.lang.Math.sqrt(lengthSquared());
    }

    /**
     * Calculates the vector's length squared.
     * Faster than Vector3.length() due to the square
     * root calculation being skipped.
     * @return The length squared.
     */
    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Normalizes the vector so that its length is 1.
     */
    public void normalize() {
        if (lengthSquared() == 0) return;
        multiply(1.0 / length());
    }

    /**
     * Normalizes the vector so that the resulting vectors' length is 1.
     * @return The normalized vector.
     */
    public Vector3 normalized() {
        if (lengthSquared() == 0) return new Vector3(this);
        return multiplied(1.0 / length());
    }

    /**
     * Multiplies the vector by another vector.
     * @param vector The vector to multiply with.
     */
    public void multiply(@NotNull Vector3 vector) {
        x *= vector.x;
        y *= vector.y;
        z *= vector.z;
    }

    /**
     * Multiplies the vector with the specified values.
     * @param x X factor.
     * @param y Y factor.
     * @param z Z factor.
     */
    public void multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    /**
     * Multiplies the vectors' values with a.
     * @param a The value to multiply with,
     */
    public void multiply(double a) {
        x *= a;
        y *= a;
        z *= a;
    }

    /**
     * Multiplies the vector with another vector.
     * @param vector The vector to multiply with.
     * @return The multiplied vector.
     */
    public Vector3 multiplied(@NotNull Vector3 vector) {
        return new Vector3(x * vector.x, y * vector.y, z * vector.z);
    }

    /**
     * Multiplies the vector with the specified values.
     * @param x X Factor.
     * @param y Y Factor.
     * @param z Z Factor.
     * @return The multiplied vector.
     */
    public Vector3 multiplied(double x, double y, double z) {
        return new Vector3(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Multiplies the vectors' values with a.
     * @param a The value to multiply with.
     * @return The multiplied vector.
     */
    public Vector3 multiplied(double a) {
        return new Vector3(x * a, y * a, z * a);
    }

    /**
     * Translates (adds to) the vector by another vector.
     * @param vector The translation vector.
     */
    public void translate(@NotNull Vector3 vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    /**
     * Translates (adds to) the vector by the specified values.
     * @param x Translation in x direction.
     * @param y Translation in y direction.
     * @param z Translation in z direction.
     */
    public void translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Translates (adds to) the vector by another vector.
     * @param vector The translation vector.
     * @return The translated vector.
     */
    public Vector3 translated(@NotNull Vector3 vector) {
        return new Vector3(x + vector.x, y + vector.y, z + vector.z);
    }

    /**
     * Translates (adds to) the vector by the specified values.
     * @param x Translation in x direction.
     * @param y Translation in y direction.
     * @param z Translation in z direction.
     * @return The translated vector.
     */
    public Vector3 translated(double x, double y, double z) {
        return new Vector3(this.x + x, this.x + y, this.z + z);
    }

    /**
     * Inverts (negates) the vector by multiplying all values by -1.
     */
    public void invert() {
        x = -x;
        y = -y;
        z = -z;
    }

    /**
     * Inverts (negates) the vector by multiplying all values by -1.
     * @return The inverted vector.
     */
    public Vector3 inverted() {
        return new Vector3(-x, -y, -z);
    }

    /**
     * Calculates the dot product between this vector and the given vector.
     * @param vector The vector to calculate the dot product with.
     * @return The calculated dot product.
     */
    public double dot(@NotNull Vector3 vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    /**
     * Calculates the angle between this vector and the given vector.
     * @param vector The vector to calculate the angle between.
     * @return The angle in radians.
     */
    public double angleTo(@NotNull  Vector3 vector) {
        return java.lang.Math.acos(dot(vector) / (length() * vector.length()));
    }

    /**
     * Calculates the cross product (a perpendicular vector) between this vector and the given vector.
     * @param vector The vector to calculate the cross product with.
     * @return A vector representing the cross product of the two vectors.
     */
    public Vector3 cross(@NotNull Vector3 vector) {
        return new Vector3(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
    }

    /**
     * Linearly interpolates between this vector and the specified vector by factor t.
     * @param vector The vector to lerp to.
     * @param t The interpolation factor (usually between 0 and 1).
     */
    public void lerp(@NotNull Vector3 vector, double t) {
        x = MathUtils.lerp(x, vector.x, t);
        y = MathUtils.lerp(y, vector.y, t);
        z = MathUtils.lerp(z, vector.z, t);
    }

    /**
     * Linearly interpolates between this vector and the specified vector by factor t.
     * @param vector The vector to lerp to.
     * @param t The interpolation factor (usually between 0 and 1).
     * @return The lerped vector.
     */
    public Vector3 lerped(@NotNull Vector3 vector, double t) {
        return new Vector3(MathUtils.lerp(x, vector.x, t), MathUtils.lerp(y, vector.y, t), MathUtils.lerp(z, vector.z, t));
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public static Vector3 midpointBetween(@NotNull Vector3 a, @NotNull Vector3 b) {
        return new Vector3((a.x + b.x) / 2, (a.y + b.y) / 2, (a.z + b.z) / 2);
    }

    public float[] toFloatArray() {
        return new float[] {(float) x, (float) y, (float) z};
    }

    @Override
    public String toString() {
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }
}
