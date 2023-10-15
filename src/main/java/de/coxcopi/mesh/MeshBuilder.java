package de.coxcopi.mesh;

import de.coxcopi.mesh.buffer.IndexBuffer;
import de.coxcopi.mesh.buffer.VertexBuffer;
import de.coxcopi.util.math.Vector3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class MeshBuilder {

    public static Mesh createMesh(float[] vertices, int[] indices) {
        final int floatsPerElement = 3;
        int vao = generateVertexArrayObject();
        VertexBuffer vb = new VertexBuffer(vertices);
        GL30.glVertexAttribPointer(0, floatsPerElement, GL11.GL_FLOAT, false, 0, 0);
        GL30.glEnableVertexAttribArray(0);
        IndexBuffer ib = new IndexBuffer(indices);
        unbind();
        return new Mesh(vao, indices.length);
    }

    public static Mesh createPrimitiveRect(float width, float height) {
        final float[] vertices = {
                -width, -height, 0,
                 width, -height, 0,
                 width,  height, 0,
                -width,  height, 0
        };
        final int[] indices = {
            0, 1, 2,
            0, 2, 3
        };
        return createMesh(vertices, indices);
    }


    public static Mesh createPrimitiveCuboid(float width, float height, float depth) {
        /*
        final float[] vertices = {
                // Bottom Quad
                -width, -height,  depth,
                width, -height,  depth,
                width, -height, -depth,
                -width, -height, -depth
                // Top Quad
                -width,  height,  depth,
                width,  height,  depth,
                width,  height, -depth,
                -width,  height, -depth
        };

        final int[] indices = {
                // Bottom
                0, 1, 2,    0, 2, 3,
                // Front
                0, 1, 5,    0, 5, 4,
                // Back
                2, 3, 7,    2, 7, 6,
                // Left
                0, 4, 7,    0, 7, 4,
                // Right
                1, 2, 6,    1, 6, 5,
                // Top
                1, 5, 6,    1, 6, 7,
        };

         */

        final float[] vertices = {
                // Bottom Vertices
                -1, -1, -1,
                1, -1, -1,
                1, -1, 1,
                -1, -1, 1,

                // Top Vertices
                -1, 1, -1,
                1, 1, -1,
                1, 1, 1,
                -1, 1, 1
        };

        final int[] indices = {
                // Bottom
                0, 3, 2,
                0, 2, 1,
                // Top
                4, 5, 7,
                5, 6, 7,
                // Back
                0, 1, 5,
                0, 5, 4,
                // Front
                6, 2, 3,
                6, 3, 7,
                // Left
                1, 2, 6,
                1, 6, 5,
                // Right
                3, 0, 4,
                3, 4, 7
        };

        return createMesh(vertices, indices);
    }

    private static int generateVertexArrayObject() {
        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        return vao;
    }

    private static void unbind() {
        GL30.glBindVertexArray(0);
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
