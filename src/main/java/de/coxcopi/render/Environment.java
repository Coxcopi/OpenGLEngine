package de.coxcopi.render;

import de.coxcopi.util.Color;

public class Environment {

    public Color ambientLightColor = new Color(1.0);
    public double ambientLightIntensity = 0.3;

    public Color getBackgroundColor() {
        return ambientLightColor.multiplied(ambientLightIntensity);
    }
}
