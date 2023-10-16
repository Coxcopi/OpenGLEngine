package de.coxcopi.render;

import de.coxcopi.mesh.Mesh;
import de.coxcopi.util.math.Math;
import de.coxcopi.util.math.Matrix4;
import de.coxcopi.util.math.Vector3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final ArrayList<Mesh> renderQueue = new ArrayList<>();
    private final long window;
    public static final Camera camera = new Camera();

    public Renderer(long window) {
        this.window = window;
        glEnable(GL_DEPTH_TEST);
    }

    public void addToRenderQueue(Mesh mesh) {
        renderQueue.add(mesh);
    }

    public void removeFromRenderQueue(Mesh mesh) {
        renderQueue.remove(mesh);
    }

    public void renderTick() {
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        for (Mesh mesh : renderQueue) {
            if (mesh == null) {
                continue;
            }
            mesh.transform.rotateX(Math.degToRad(1.1));
            mesh.transform.rotateY(Math.degToRad(0.8));
            mesh.transform.rotateZ(Math.degToRad(-1));

            double r = 10;
            double camX = java.lang.Math.sin(glfwGetTime()) * r;
            double camZ = java.lang.Math.cos(glfwGetTime()) * r;

            camera.viewMatrix = Matrix4.lookAt(new Vector3(camX, 0, camZ), new Vector3(0), new Vector3(0, 1, 0));

            mesh.render();
            GL20.glDrawElements(GL20.GL_TRIANGLES, mesh.getElementCount(), GL20.GL_UNSIGNED_INT, 0);

        }
        glfwSwapBuffers(window);
        glfwPollEvents();
    }
}
