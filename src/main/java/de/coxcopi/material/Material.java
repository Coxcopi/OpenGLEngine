package de.coxcopi.material;

import de.coxcopi.material.shader.Shader;
import de.coxcopi.material.shader.ShaderParser;
import de.coxcopi.util.Color;

public class Material {

    public Color ambient = new Color(0.329412, 0.223529, 0.027451);
    public Color diffuse = new Color(0.780392, 0.568627, 0.113725);
    public Color specular = new Color(0.992157, 0.941176, 0.807843);
    public double shininess = 27.8974;
    public final Shader shader;

    public Material() {
        this.shader = ShaderParser.getDefaultShader();
    }

    public void bind() {
        shader.bind();
    }

    public void unbind() {
        shader.unbind();
    }
}
