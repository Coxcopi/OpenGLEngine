package de.coxcopi.render;

import de.coxcopi.input.Input;
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

    private double y = 0;

    private final ArrayList<Mesh> renderQueue = new ArrayList<>();
    public long window;
    public static final Camera camera = new Camera(60.0, 800.0 / 600.0);
    public static final Environment environment = new Environment();
    private double lastFrame = 0.0;
    private double camSpeed = 2.5;

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
        // Set key press callback
        glfwSetKeyCallback(window, Input::processKeyCallback);
        // Set mouse position callback
        glfwSetCursorPosCallback(window, Input::processCursorPosCallback);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

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
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
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

        double currentFrame = glfwGetTime();
        double deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        double mouseSensitivity = 0.1;

        Vector3 vel = new Vector3();
        if (Input.isKeyPressed(GLFW_KEY_W)) {
            vel.translate(camera.transform.getZ());
        }
        if (Input.isKeyPressed(GLFW_KEY_S)) {
            vel.translate(camera.transform.getZ().inverted());
        }
        if (Input.isKeyPressed(GLFW_KEY_A)) {
            vel.translate(camera.transform.getX());
        }
        if (Input.isKeyPressed(GLFW_KEY_D)) {
            vel.translate(camera.transform.getX().inverted());
        }
        if (Input.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            camSpeed += 0.1;
        }
        if (Input.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            camSpeed -= 0.1;
        }
        camSpeed = MathUtils.clamp(camSpeed, 0.0, 30.0);
        vel = vel.normalized().multiplied(camSpeed * deltaTime);

        double mX = Input.getMouseDeltaX() * mouseSensitivity * deltaTime;
        double mY = Input.getMouseDeltaY() * mouseSensitivity * deltaTime;

        camera.rotate(-mX, -mY);

        camera.transform.translate(vel);
        camera.recalculateViewMatrix();

        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        final Color bg = environment.getBackgroundColor();
        glClearColor((float) bg.r, (float) bg.g, (float) bg.b, 1f);

        for (Mesh mesh : renderQueue) {
            if (mesh == null || !mesh.visible) {
                continue;
            }

            mesh.transform.rotateY(0.01);

            mesh.render();
            GL20.glDrawElements(GL_TRIANGLES, mesh.getElementCount(), GL20.GL_UNSIGNED_INT, 0);
        }
        //System.out.println(Math.round(1.0 / deltaTime));
        glfwSwapBuffers(window);
        Input.update();
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
