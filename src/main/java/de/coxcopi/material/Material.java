package de.coxcopi.material;

import de.coxcopi.material.shader.Shader;
import de.coxcopi.material.shader.ShaderParser;

public class Material {

    // TODO: Extend with color and texture fields
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
