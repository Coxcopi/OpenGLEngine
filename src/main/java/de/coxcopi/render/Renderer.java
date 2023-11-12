package de.coxcopi.render;

import de.coxcopi.mesh.Mesh;
import de.coxcopi.util.Color;
import de.coxcopi.util.math.MathUtils;
import de.coxcopi.util.math.Vector3;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Renderer {

    private final ArrayList<Mesh> renderQueue = new ArrayList<>();
    public long window;
    public static final Camera camera = new Camera(new Vector3(0, 5, 5), new Vector3(0), 60.0, 800.0 / 600.0);
    public static final Environment environment = new Environment();

    /**
     * Initializes GLFW, OpenGL and the LWJGL context. Creates a new window.
     * @return The glfw window.
     */
    public long init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Window creation
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        window = glfwCreateWindow(800, 600, "GLFW Test", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        // Key press registering
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get resolution of primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pWidth.get(0)) / 2
            );
        } // The stack frame is popped automatically

        glfwMakeContextCurrent(window);
        // Enable V-Sync
        glfwSwapInterval(1);
        // Make window visible
        glfwShowWindow(window);
        // Set Window Resize Callback
        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                onWindowResize(l, i, i1);
            }
        });

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        setParams();

        return window;
    }

    private void setParams() {
        glEnable(GL_DEPTH_TEST);
    }

    public void addToRenderQueue(Mesh mesh) {
        if (renderQueue.contains(mesh)) {
            return;
        }
        renderQueue.add(mesh);
    }

    public void removeFromRenderQueue(Mesh mesh) {
        renderQueue.remove(mesh);
    }

    public boolean isInRenderQueue(Mesh mesh) {
        return renderQueue.contains(mesh);
    }

    public void renderTick() {
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        final Color bg = environment.getBackgroundColor();
        glClearColor((float) bg.r, (float) bg.g, (float) bg.b, 1f);

        for (Mesh mesh : renderQueue) {
            if (mesh == null || !mesh.visible) {
                continue;
            }
            //mesh.transform.rotateX(MathUtils.degToRad(0.5));
            mesh.transform.rotateY(MathUtils.degToRad(0.8));
            //mesh.transform.rotateZ(MathUtils.degToRad(-1));

            double r = 6;
            double camX = java.lang.Math.sin(glfwGetTime()) * r;
            double camZ = java.lang.Math.cos(glfwGetTime()) * r;

            //camera.viewMatrix = Matrix4.lookAt(new Vector3(camX, 5, camZ), new Vector3(0), new Vector3(0, 1, 0));

            mesh.render();
            GL20.glDrawElements(GL_TRIANGLES, mesh.getElementCount(), GL20.GL_UNSIGNED_INT, 0);

        }
        glfwSwapBuffers(window);
        glfwPollEvents();
    }


    private void onWindowResize(long window, int width, int height) {
        GL30.glViewport(0, 0, width, height);
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
