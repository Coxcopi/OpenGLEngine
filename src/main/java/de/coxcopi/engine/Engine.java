package de.coxcopi.engine;

import de.coxcopi.mesh.Mesh;
import de.coxcopi.mesh.MeshParser;
import de.coxcopi.render.Renderer;
import org.lwjgl.Version;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {
    public static Renderer renderer = new Renderer();
    private static long lastNanoTime = 0;
    private static final ArrayList<Model> models = new ArrayList<>();
    private static boolean initialized = false;

    /**
     * Instances a model, calls its init() method and adds it to the render queue.
     * If the model has already been instanced, an error is thrown.
     * @param model The model to instance.
     */
    public static void instance(Model model) {
        if (!initialized) {
            throw new IllegalStateException("Engine is not initialized.");
        }
        if (models.contains(model)) {
            throw new RuntimeException("Trying to instance the same model twice.");
        }
        models.add(model);
        if (model.mesh != null) {
            renderer.addToRenderQueue(model.mesh);
        }
        model.init();
    }

    /**
     * Frees (removes) a model from the engine.
     * Does nothing if the model hasn't been instanced.
     * @param model The model to free.
     */
    public static void free(Model model) {
        if (model.mesh != null) {
            renderer.removeFromRenderQueue(model.mesh);
        }
        models.remove(model);
    }

    /**
     * Initializes the Engine.
     * NOTE: This has to be called before everything else, since it initializes the LWJGL and OpenGL context.
     */
    public static void init() {
        renderer.init();
        initialized = true;
        System.out.println("Engine initialized. Running LWJGL Version " + Version.getVersion() + ".");
    }

    /**
     * Runs the Engines main loop until the escape key is pressed. Throws an error if the Engine
     * hasn't been initialized previously.
     */
    public static void run() {
        if (!initialized) {
            throw new IllegalStateException("Engine is not initialized.");
        }
        // Main Loop
        while (!glfwWindowShouldClose(renderer.window)) {
            update();
        }
        exit();
    }

    /**
     * Called as soon as the main loop has exited. Destroys the renderer and exits the application.
     */
    private static void exit() {
        renderer.destroy();
        System.exit(0);
    }

    /**
     * Called every frame. Updates all models.
     */
    private static void update() {
        // Time since last update() call;
        double delta = calcDeltaTime();
        // Run update() method for each object.
        for (Model model : models) {
            if (model.disabled) {
                continue;
            }
            model.update(delta);
        }
        // Render the frame.
        renderer.renderTick();
    }

    /**
     * Calculates the time between the last frame and the previous frame. Uses System.nanoTime()
     * internally in order to achieve a more precise result.
     * @return The delta time (in seconds).
     */
    private static double calcDeltaTime() {
        final double delta = (System.nanoTime() - lastNanoTime) / 1000000000.0;
        lastNanoTime = System.nanoTime();
        return delta;
    }
}
