package de.coxcopi.render;

import de.coxcopi.util.Color;

public class Environment {

    public Color ambientLightColor = Color.BLACK;
    public double ambientLightIntensity = 1.0;

    public Color getBackgroundColor() {
        return ambientLightColor.multiplied(ambientLightIntensity);
    }
}
