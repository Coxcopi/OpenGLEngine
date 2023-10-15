package de.coxcopi.material.shader;

public class ShaderProgramSource {

    final String vertexSource;
    final String fragmentSource;

    public ShaderProgramSource(String vertexSource, String fragmentSource) {
        this.vertexSource = vertexSource;
        this.fragmentSource = fragmentSource;
    }
}
