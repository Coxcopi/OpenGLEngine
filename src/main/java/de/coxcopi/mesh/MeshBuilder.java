package de.coxcopi.mesh;

import de.coxcopi.mesh.buffer.IndexBuffer;
import de.coxcopi.mesh.buffer.VertexBuffer;
import de.coxcopi.util.math.Vector3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static Mesh createPrimitiveSphere(int ringsLongitude, int ringsLatitude, double radius) {
        double phiStep = 2 * java.lang.Math.PI / ringsLongitude;
        double thetaStep = Math.PI / ringsLatitude;
        int numVertices = 2 + ringsLongitude * (ringsLatitude - 1);
        int numIndices = 2 * 3 * ringsLongitude + 2 * 3 * ringsLongitude * (ringsLatitude - 2);

        Vector3[] vertices = new Vector3[numVertices];
        int c = 0;

        // Top Vertex
        vertices[c++] = new Vector3(0, radius, 0);

        for (int j = 0; j < (ringsLatitude - 1); j++) {
            double theta = j * thetaStep;
            for (int i = 0; i < ringsLongitude; i++) {
                double phi = i * phiStep;
                vertices[c++] = new Vector3(
                        radius * java.lang.Math.sin(theta) * java.lang.Math.cos(phi),
                        radius * java.lang.Math.cos(theta),
                        radius * java.lang.Math.sin(theta) * java.lang.Math.sin(phi)
                );
            //Vector3.sphericalToCartesian(radius, theta, phi);
            }
        }

        // Bottom Vertex
        vertices[c++] = new Vector3(0, -radius, 0);

        c = 0;
        System.out.println(numIndices);
        final int[] indices = new int[numIndices];

        // Top cap indices, connecting the north pole to the first ring
        for (int i = 0; i < (ringsLongitude - 1); i++) {
            indices[c++] = 0;
            indices[c++] = i + 1;
            indices[c++] = i + 2;
        }

        indices[c++] = 0;
        indices[c++] = ringsLongitude;
        indices[c++] = 1;

        // Indices for the section between top and bottom
        for (int j = 0; j < (ringsLatitude - 2); j++) {
            for (int i = 0; i < (ringsLongitude - 1); i++) {
                int[] index = {
                    1 + i + j * ringsLongitude,
                    1 + i + (j + 1) * ringsLongitude,
                    1 + (i + 1) + (j + 1) * ringsLongitude,
                    1 + (i + 1) + j * ringsLongitude
                };

                indices[c++] = index[0];
                indices[c++] = index[1];
                indices[c++] = index[2];

                indices[c++] = index[0];
                indices[c++] = index[2];
                indices[c++] = index[3];
            }

            int[] index = {
                    ringsLongitude + j * ringsLongitude,
                    ringsLongitude + (j + 1) * ringsLongitude,
                    1 + (j + 1) * ringsLongitude,
                    1 + j * ringsLongitude
            };

            indices[c++] = index[0];
            indices[c++] = index[1];
            indices[c++] = index[2];

            indices[c++] = index[0];
            indices[c++] = index[2];
            indices[c++] = index[3];

        }

        // Bottom cap indices
        final int southPoleIndex = vertices.length - 1;
        for (int i = 0; i < (ringsLongitude - 1); i++) {
            indices[c++] = southPoleIndex;
            indices[c++] = southPoleIndex - ringsLongitude + i + 1;
            indices[c++] = southPoleIndex - ringsLongitude + i;
        }

        indices[c++] = southPoleIndex;
        indices[c++] = southPoleIndex - ringsLongitude;
        indices[c++] = southPoleIndex - 1;

        c = 0;

        float[] verts = new float[numVertices * 3];
        for (Vector3 vertex : vertices) {
            verts[c++] = (float) vertex.x;
            verts[c++] = (float) vertex.y;
            verts[c++] = (float) vertex.z;
        }
        return createMesh(verts, indices);
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
