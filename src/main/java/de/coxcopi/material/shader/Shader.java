package de.coxcopi.material.shader;

import de.coxcopi.material.Material;
import de.coxcopi.render.Environment;
import de.coxcopi.util.Color;
import de.coxcopi.util.math.Matrix4;
import de.coxcopi.util.math.Vector3;
import org.lwjgl.opengl.GL20;

public class Shader {
    final ShaderProgramSource source;
    final int programID;

    public Shader(ShaderProgramSource source, int programID) {
        this.source = source;
        this.programID = programID;
    }

    public void setDefaultMatrixUniforms(Matrix4 modelMatrix, Matrix4 viewMatrix, Matrix4 projectionMatrix) {
        GL20.glUniformMatrix4fv(2, false, modelMatrix.toUniformFloatArray());
        GL20.glUniformMatrix4fv(3, false, viewMatrix.toUniformFloatArray());
        GL20.glUniformMatrix4fv(4, false, projectionMatrix.toUniformFloatArray());
    }

    /*
    public void setLightingUniforms(Color ambientLightColor, Vector3 viewPosition) {
        setUniform("ambientColor", ambientLightColor, false);
        setUniform("viewPosition", viewPosition);
    }
     */

    public void setLightingUniforms(Environment environment, Material material, Vector3 viewPosition) {
        // TODO: Expand (?)
        setUniform("environment.ambient", environment.getBackgroundColor(), false);
        setUniform("material.ambient", material.ambient, false);
        setUniform("material.diffuse", material.diffuse, false);
        setUniform("material.specular", material.specular, false);
        setUniform("material.shininess", material.shininess);

        setUniform("viewPosition", viewPosition);
    }

    public void setUniform(String uniform, double value) {
        setUniform(uniform, (float) value);
    }

    public void setUniform(String uniform, float value) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {
            return;
        }
        GL20.glUniform1f(location, value);
    }

    public void setUniform(String uniform, int value) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {
            return;
        }
        GL20.glUniform1i(location, value);
    }

    public void setUniform(String uniform, Matrix4 value, boolean transpose) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {
            return;
        }
        GL20.glUniformMatrix4fv(location, transpose, value.toUniformFloatArray());
    }

    public void setUniform(String uniform, Vector3 value) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {
            return;
        }
        GL20.glUniform3fv(location, value.toFloatArray());
    }

    public void setUniform(String uniform, Color value, boolean useAlpha) {
        final int location = getUniformLocation(uniform);
        if (location == -1) {
            return;
        }
        if (useAlpha) {
            GL20.glUniform4fv(location, value.toUniformFloatArray());
        } else {
            GL20.glUniform3fv(location, value.toUniformFloatArrayNoAlpha());
        }
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
