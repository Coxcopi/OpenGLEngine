package de.coxcopi.mesh.buffer;

import org.lwjgl.opengl.GL15;

public class VertexBuffer {

    final int id;

    public VertexBuffer(float[] data) {
        id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
    }

    public void bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
