package de.coxcopi.util.math;

import org.jetbrains.annotations.NotNull;

public class Vector3 {

    public double x;
    public double y;
    public double z;

    public Vector3() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(@NotNull Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vector3(double a) {
        this.x = a;
        this.y = a;
        this.z = a;
    }

    public double length() {
        return java.lang.Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public void normalize() {
        multiply(1.0 / length());
    }

    public Vector3 normalized() {
        return multiplied(1.0 / length());
    }

    public void multiply(@NotNull Vector3 vector) {
        x *= vector.x;
        y *= vector.y;
    }

    public void multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    public void multiply(double a) {
        x *= a;
        y *= a;
        z *= a;
    }

    public Vector3 multiplied(@NotNull Vector3 vector) {
        return new Vector3(x * vector.x, y * vector.y, z * vector.z);
    }

    public Vector3 multiplied(double x, double y, double z) {
        return new Vector3(this.x * x, this.y * y, this.z * z);
    }

    public Vector3 multiplied(double a) {
        return new Vector3(x * a, y * a, z * a);
    }

    public void translate(@NotNull Vector3 vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public void translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public Vector3 translated(@NotNull Vector3 vector) {
        return new Vector3(x + vector.x, y + vector.y, z + vector.z);
    }

    public Vector3 translated(double x, double y, double z) {
        return new Vector3(this.x + x, this.x + y, this.z + z);
    }

    public void invert() {
        x = -x;
        y = -y;
        z = -z;
    }

    public Vector3 inverted() {
        return new Vector3(-x, -y, -z);
    }

    public double dot(@NotNull Vector3 vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    public double angleTo(Vector3 vector) {
        return java.lang.Math.acos(dot(vector) / (length() * vector.length()));
    }

    public Vector3 cross(@NotNull Vector3 vector) {
        return new Vector3(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
    }

    public void lerp(@NotNull Vector3 vector, double t) {
        x = Math.lerp(x, vector.x, t);
        y = Math.lerp(y, vector.y, t);
        z = Math.lerp(z, vector.z, t);
    }

    public Vector3 lerped(@NotNull Vector3 vector, double t) {
        return new Vector3(Math.lerp(x, vector.x, t), Math.lerp(y, vector.y, t), Math.lerp(z, vector.z, t));
    }

    @Override
    public String toString() {
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }
}
