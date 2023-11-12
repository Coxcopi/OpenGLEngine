package de.coxcopi.render;

import de.coxcopi.util.math.Matrix4;
import de.coxcopi.util.math.Vector3;

public class Camera {

    private Vector3 position;
    private Vector3 lookingAt;
    private double fov;
    private double aspectRatio;
    private double near = 0.1;
    private double far = 100.0;
    public Matrix4 viewMatrix;
    public Matrix4 projectionMatrix;

    public Camera(Vector3 position, Vector3 lookingAt, double fov, double aspectRatio) {
        this.position = position;
        this.lookingAt = lookingAt;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        recalculateViewMatrix();
        recalculateProjectionMatrix();
    }

    public void setLookingAt(Vector3 lookingAt) {
        this.lookingAt = lookingAt;
        recalculateViewMatrix();
    }

    public void setPosition(Vector3 position) {
        this.position = position;
        recalculateViewMatrix();
    }

    public void setFieldOfView(double fov) {
        this.fov = fov;
        recalculateProjectionMatrix();
    }

    public void setViewPlane(double near, double far) {
        this.near = near;
        this.far = far;
        recalculateProjectionMatrix();
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getLookingAt() {
        return lookingAt;
    }

    public double getFov() {
        return fov;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    private void recalculateViewMatrix() {
        viewMatrix = Matrix4.lookAt(position, lookingAt, new Vector3(0, 1, 0));
    }

    private void recalculateProjectionMatrix() {
        projectionMatrix = Matrix4.perspective(fov, aspectRatio, near, far);
    }
}
