package de.coxcopi.util.math;

import org.jetbrains.annotations.NotNull;

public class Vector2 {

    public double x;
    public double y;

    public static final Vector2 UP = new Vector2(0.0, -1.0);
    public static final Vector2 DOWN = new Vector2(-1.0, 1.0);
    public static final Vector2 LEFT = new Vector2(-1.0, 0.0);
    public static final Vector2 RIGHT = new Vector2(1.0, 0.0);

    public Vector2() {
        this.x = 0.0f;
        this.y = 0.0f;
    }
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(@NotNull Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vector2(double a) {
        this.x = a;
        this.y = a;
    }

    public double length() {
        return java.lang.Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public void normalize() {
        multiply(1.0 / length());
    }

    public Vector2 normalized() {
        return multiplied(1.0 / length());
    }

    public void multiply(@NotNull Vector2 vector) {
        x *= vector.x;
        y *= vector.y;
    }

    public void multiply(double x, double y) {
        this.x *= x;
        this.y *= y;
    }

    public void multiply(double a) {
        x *= a;
        y *= a;
    }

    public Vector2 multiplied(@NotNull Vector2 vector) {
        return new Vector2(x * vector.x, y * vector.y);
    }

    public Vector2 multiplied(double x, double y) {
        return new Vector2(this.x * x, this.y * y);
    }

    public Vector2 multiplied(double a) {
        return new Vector2(x * a, y * a);
    }

    public void translate(@NotNull Vector2 vector) {
        x += vector.x;
        y += vector.y;
    }

    public void translate(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public Vector2 translated(@NotNull Vector2 vector) {
        return new Vector2(x + vector.x, y + vector.y);
    }

    public Vector2 translated(double x, double y) {
        return new Vector2(this.x + x, this.x + y);
    }

    public void invert() {
        x = -x;
        y = -y;
    }

    public Vector2 inverted() {
        return new Vector2(-x, -y);
    }

    public double dot(@NotNull Vector2 vector) {
        return x * vector.x + y * vector.y;
    }

    public double angleTo(Vector2 vector) {
        return java.lang.Math.acos(dot(vector) / (length() * vector.length()));
    }

    public void lerp(@NotNull Vector2 vector, double t) {
        x = Math.lerp(x, vector.x, t);
        y = Math.lerp(y, vector.y, t);
    }

    public Vector2 lerped(@NotNull Vector2 vector, double t) {
        return new Vector2(Math.lerp(x, vector.x, t), Math.lerp(y, vector.y, t));
    }
}
