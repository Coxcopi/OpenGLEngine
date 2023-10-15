package de.coxcopi.material.shader;

import de.coxcopi.util.math.Matrix4;
import org.lwjgl.opengl.GL20;

public class Shader {
    final String vertexSource;
    final String fragmentSource;
    final int programID;
    public enum DefaultMatrixUniform {
        MODEL_MATRIX,
        VIEW_MATRIX,
        PROJECTION_MATRIX
    }

    public Shader(String vertexSource, String fragmentSource, int programID) {
        this.vertexSource = vertexSource;
        this.fragmentSource = fragmentSource;
        this.programID = programID;
    }

    public void setDefaultMatrixUniforms(Matrix4 modelMatrix, Matrix4 viewMatrix, Matrix4 projectionMatrix) {
        GL20.glUniformMatrix4fv(1, false, modelMatrix.toUniformFloatArray());
        GL20.glUniformMatrix4fv(2, false, viewMatrix.toUniformFloatArray());
        GL20.glUniformMatrix4fv(3, false, projectionMatrix.toUniformFloatArray());
    }

    public void setDefaultMatrixUniform(DefaultMatrixUniform type, Matrix4 value, boolean transpose) {
        final int location = (type == DefaultMatrixUniform.MODEL_MATRIX ? 1 : (type == DefaultMatrixUniform.VIEW_MATRIX ? 2 : 3));
        GL20.glUniformMatrix4fv(location, transpose, value.toUniformFloatArray());
    }

    public void setUniform(String uniform, double value) {
        setUniform(uniform, (float) value);
    }

    public void setUniform(String uniform, float value) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {return;}
        GL20.glUniform1f(location, value);
    }

    public void setUniform(String uniform, int value) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {return;}
        GL20.glUniform1i(location, value);
    }

    public void setUniform(String uniform, Matrix4 value, boolean transpose) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {return;}
        GL20.glUniformMatrix4fv(location, transpose, value.toUniformFloatArray());
    }

    private int getUniformLocation(String uniform) {
        return GL20.glGetUniformLocation(programID, uniform);
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }
}
