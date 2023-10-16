package de.coxcopi;

import de.coxcopi.material.Material;
import de.coxcopi.mesh.Mesh;
import de.coxcopi.mesh.MeshBuilder;
import de.coxcopi.render.Renderer;
import de.coxcopi.util.math.Vector3;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Test {

    private long window;

    public void run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        renderTest();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
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

    }

    private void renderTest() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Renderer renderer = new Renderer(window);

        Mesh quad = MeshBuilder.createPrimitiveRect(1f, 1f);
        quad.transform.translate(new Vector3(3, -2, 0));

        Mesh cube = MeshBuilder.createPrimitiveCuboid(1f, 1f, 1f);
        cube.transform.translate(new Vector3(0, 0, 0));
        cube.transform.scale(0.6);

        Material material = new Material();
        quad.material = material;
        cube.material = material;

        renderer.addToRenderQueue(quad);
        renderer.addToRenderQueue(cube);

        while (!glfwWindowShouldClose(window)) {
            renderer.renderTick();
        }
    }
}
