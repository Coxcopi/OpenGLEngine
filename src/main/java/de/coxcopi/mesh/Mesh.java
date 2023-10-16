package de.coxcopi.mesh;

import de.coxcopi.render.Renderer;
import de.coxcopi.material.Material;
import de.coxcopi.util.math.Matrix4;
import org.lwjgl.opengl.GL30;

public class Mesh {

    private final int vao;
    private final int elements;
    public Matrix4 transform;
    public Material material;
    public Mesh(int vao, int elements) {
        this.vao = vao;
        this.elements = elements;
        this.transform = Matrix4.transform();
    }

    public int getElementCount() {
        return elements;
    }

    public void render() {
        material.bind();
        material.shader.setDefaultMatrixUniforms(transform, Renderer.camera.viewMatrix, Renderer.camera.projectionMatrix);
        GL30.glBindVertexArray(vao);
    }
}
