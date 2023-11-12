package de.coxcopi.mesh;

import de.coxcopi.render.Renderer;
import de.coxcopi.material.Material;
import de.coxcopi.util.math.Matrix4;
import org.lwjgl.opengl.GL30;

public class Mesh {

    public Matrix4 transform;
    public Material material;
    /**
     * If false, the mesh is not rendered.
     */
    public boolean visible;
    private final int vao;
    private final int elements;

    public Mesh(int vao, int elements) {
        this.vao = vao;
        this.elements = elements;
        this.transform = Matrix4.transform();
        this.material = new Material();
        this.visible = true;
    }

    public int getElementCount() {
        return elements;
    }

    public void render() {
        if (!visible) {
            return;
        }
        material.bind();
        material.shader.setDefaultMatrixUniforms(transform, Renderer.camera.viewMatrix, Renderer.camera.projectionMatrix);
        material.shader.setLightingUniforms(Renderer.environment.getBackgroundColor(), Renderer.camera.getPosition());
        GL30.glBindVertexArray(vao);
    }
}
