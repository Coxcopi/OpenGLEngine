package de.coxcopi.render;

import de.coxcopi.util.math.MathUtils;
import de.coxcopi.util.math.Matrix4;
import de.coxcopi.util.math.Vector3;

public class Camera {
    private double fov;
    private double aspectRatio;
    private double near = 0.1;
    private double far = 100.0;
    public Matrix4 viewMatrix;
    public Matrix4 transform = Matrix4.transform();
    public Matrix4 projectionMatrix;
    public Vector3 cameraFront = new Vector3(0, 0, -1);
    private double pitch = 0;
    private double yaw = 0;

    public Camera(double fov, double aspectRatio) {
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        recalculateViewMatrix();
        recalculateProjectionMatrix();
    }

    public void rotate(double yaw, double pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
        recalculateCameraFront();
    }

    public void setRotations(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        recalculateCameraFront();
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

    public double getFov() {
        return fov;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    private void recalculateCameraFront() {
        final Vector3 direction = new Vector3();
        direction.x = Math.cos(yaw) * Math.cos(pitch);
        direction.y = Math.sin(pitch);
        direction.z = Math.sin(yaw) * Math.cos(pitch);
        cameraFront = direction.normalized();
        final Vector3 z = new Vector3(cameraFront);
        final Vector3 yTemp = Vector3.UP();
        final Vector3 x = z.cross(yTemp);
        final Vector3 y = z.cross(x);
        transform.setX(x);
        transform.setY(y);
        transform.setZ(z);
    }

    public void recalculateViewMatrix() {
        //transform.multiply(transform.getOrigin().translated(new Vector3(0, 0, -1)))
        viewMatrix = Matrix4.lookAtNew(transform.getOrigin(), transform.getOrigin().translated(cameraFront), new Vector3(0, 1, 0));
    }

    private void recalculateProjectionMatrix() {
        projectionMatrix = Matrix4.perspective(fov, aspectRatio, near, far);
    }
}
