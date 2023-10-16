package de.coxcopi.render;

import de.coxcopi.util.math.Matrix4;
import de.coxcopi.util.math.Vector3;

public class Camera {
    public Matrix4 viewMatrix = Matrix4.lookAt(new Vector3(0, 0, 1), new Vector3(0), new Vector3(0, 1, 0));
    // TODO: Adjust aspect dynamically according to screen size
    public Matrix4 projectionMatrix = Matrix4.perspective(70, 800.0 / 600.0, 0.01, 100.0);

    // TODO: Implement helper methods for changing fov, camera position, etc.
}
