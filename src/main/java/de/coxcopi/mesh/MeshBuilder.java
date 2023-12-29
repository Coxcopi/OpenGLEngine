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
        int vao = generateVertexArrayObject();
        VertexBuffer vb = new VertexBuffer(vertices);
        GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * 4, 0);       // Vertex Positions
        GL30.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * 4, 3 * 4);   // Vertex Normals
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        IndexBuffer ib = new IndexBuffer(indices);
        unbind();
        return new Mesh(vao, indices.length);
    }

    public static Mesh createPrimitiveRect(float width, float height) {
        final float[] vertices = {
                -width, -height, 0, 0, 0, -1,
                 width, -height, 0, 0, 0, -1,
                 width,  height, 0, 0, 0, -1,
                -width,  height, 0, 0, 0, -1,
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
                1, -1, -1,     0, -1, 0,
                -1, -1, -1,    0, -1, 0,
                -1, -1, 1,     0, -1, 0,
                1, -1, 1,      0, -1, 0,
                // Top Vertices
                -1, 1, -1,     0, 1, 0,
                1, 1, -1,      0, 1, 0,
                1, 1, 1,       0, 1, 0,
                -1, 1, 1,      0, 1, 0,
                // Back Vertices
                1, -1, -1,     0, 0, 1,
                1, 1, -1,      0, 0, 1,
                -1, 1, -1,     0, 0, 1,
                -1, -1, -1,    0, 0, 1,
                // Front Vertices
                -1, -1, 1,     0, 0, -1,
                1, -1, 1,      0, 0, -1,
                1, 1, 1,       0, 0, -1,
                -1, 1, 1,      0, 0, -1,
                // Left Vertices
                -1, -1, -1,   -1, 0, 0,
                -1, -1, 1,    -1, 0, 0,
                -1, 1, -1,    -1, 0, 0,
                -1, 1, 1,     -1, 0, 0,
                // Right Vertices
                1, -1, 1,      1, 0, 0,
                1, -1, -1,     1, 0, 0,
                1, 1, -1,      1, 0, 0,
                1, 1, 1,       1, 0, 0
        };

        final int[] indices = {
                // Top
                0, 1, 2,
                0, 2, 3,
                // Bottom
                4, 5, 6,
                4, 6, 7,
                // Back
                8, 9, 10,
                8, 10, 11,
                // Front
                12, 13, 14,
                12, 14, 15,
                // Left
                16, 17, 19,
                16, 19, 18,
                // Right
                20, 21, 22,
                20, 22, 23
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
        vertices[c] = new Vector3(0, -radius, 0);

        c = 0;
        System.out.println(numIndices);
        final int[] indices = new int[numIndices];

        // Top cap indices, connecting the North Pole to the first ring
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

    public static Mesh createPrimitiveOtherSphere(int subdivisions, double radius) {
        final float rhf = (float) radius * 0.5f;
        final float h = rhf * 0.5f * (float) Math.sqrt(2);
        // Base Shape
        float[] vertices = new float[] {
                0, -h, rhf,      // 0 (bottom triangle)
                rhf, -h, -rhf,   // 1 (bottom triangle)
                -rhf, -h, -rhf,  // 2 (bottom triangle)
                //0, h, 0
        };

        int[] indices = new int[] {
                0, 1, 2,
                //0, 1, 3,
                //1, 2, 3,
                //2, 0, 3
        };

        // Subdivisions

        Vector3[] verticesNew = new Vector3[(vertices.length / 3) * 2];
        System.out.println(verticesNew.length);
        int c = 0;

        // Vertices
        for (int i = 0; i < (indices.length / 3); i += 3) {
            final int[] ins = new int[] {indices[i], indices[i + 1], indices[i + 2]};
            Vector3 vert1 = new Vector3(vertices[ins[0]], vertices[ins[0] + 1], vertices[ins[0] + 2]);
            Vector3 vert2 = new Vector3(vertices[ins[1]], vertices[ins[1] + 1], vertices[ins[1] + 2]);
            Vector3 vert3 = new Vector3(vertices[ins[2]], vertices[ins[2] + 1], vertices[ins[2] + 2]);
            Vector3 center = Vector3.midpointBetween(Vector3.midpointBetween(vert1, vert2), vert3);
            verticesNew[c++] = vert1;
            verticesNew[c++] = vert2;
            verticesNew[c++] = vert3;
            verticesNew[c++] = center;
        }

        int[] indicesNew = new int[indices.length * 3];
        c = 0;

        // Indices

        for (int i = 0; i < verticesNew.length; i += 4) {
            int[] index = {
                    i, i + 1, i + 3,
                    i + 1, i + 2, i + 3,
                    i + 2, i, i + 3
            };
            for (int j = 0; j < index.length; j++) {
                indicesNew[c++] = index[j];
            }
        }

        System.out.println(verticesNew.length);
        System.out.println(indicesNew.length);

        return createMesh(Vector3ArrayToFloatArray(verticesNew), indicesNew);
    }

    private static int generateVertexArrayObject() {
        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        return vao;
    }

    private static float[] Vector3ArrayToFloatArray(Vector3[] vector3s) {
        final float[] floats = new float[vector3s.length * 3];
        int c = 0;
        for (Vector3 vector : vector3s) {
            floats[c++] = (float) vector.x;
            floats[c++] = (float) vector.y;
            floats[c++] = (float) vector.z;
        }
        return floats;
    }

    private static void unbind() {
        GL30.glBindVertexArray(0);
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
