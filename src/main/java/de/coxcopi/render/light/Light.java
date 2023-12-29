package de.coxcopi.render.light;

import de.coxcopi.util.Color;
import de.coxcopi.util.math.Matrix4;

public class Light {

    public Matrix4 transform = Matrix4.transform();
    public double intensity = 1.0;
    public Color lightColor = new Color(1.0);

    public Light() {
    }

    public Light(Matrix4 transform, double intensity, Color lightColor) {
        this.transform = transform;
        this.intensity = intensity;
        this.lightColor = lightColor;
    }
}
