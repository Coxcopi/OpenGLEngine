package de.coxcopi.render;

import de.coxcopi.util.math.Matrix4;
import de.coxcopi.util.math.Vector3;

public class Camera {
    public Matrix4 viewMatrix = Matrix4.lookAt(new Vector3(0, 0, 1), new Vector3(0), new Vector3(0, 1, 0));
}
