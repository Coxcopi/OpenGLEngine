package de.coxcopi.mesh;

import de.coxcopi.render.Renderer;
import de.coxcopi.material.Material;
import de.coxcopi.util.math.Matrix4;
import org.lwjgl.opengl.GL30;

public class Mesh {

    /**
     * The mesh's global (world) transform.
     */
    public Matrix4 transform;
    /**
     * The mesh's material.
     */
    public Material material;
    /**
     * If false, the mesh is not rendered.
     */
    public boolean visible;
    /**
     * The vertex attribute object.
     */
    private final int vao;
    /**
     * Amount of elements (indices).
     */
    private final int elements;

    /**
     * Constructs a new mesh with the given parameters.
     * @param vao The vertex attribute object.
     * @param elements The amount of elements (indices).
     */
    public Mesh(int vao, int elements) {
        this.vao = vao;
        this.elements = elements;
        this.transform = Matrix4.transform();
        this.material = new Material();
        this.visible = true;
    }

    /**
     * @return The amount of indices.
     */
    public int getElementCount() {
        return elements;
    }

    /**
     * Renders the mesh by binding the material and shaders,
     * setting shader uniforms and finally binding
     * the vao.
     */
    public void render() {
        if (!visible) {
            return;
        }
        material.bind();
        material.shader.setDefaultMatrixUniforms(transform, Renderer.camera.viewMatrix, Renderer.camera.projectionMatrix);
        //material.shader.setLightingUniforms(Renderer.environment.getBackgroundColor(), Renderer.camera.transform.getOrigin());
        material.shader.setLightingUniforms(Renderer.environment, material, Renderer.camera.transform.getOrigin());
        GL30.glBindVertexArray(vao);
    }
}
