package de.coxcopi;

import de.coxcopi.material.Material;
import de.coxcopi.material.shader.Shader;
import de.coxcopi.material.shader.ShaderParser;
import de.coxcopi.render.Renderer;
import de.coxcopi.util.math.Matrix4;
import de.coxcopi.util.math.Vector2;
import de.coxcopi.mesh.Mesh;
import de.coxcopi.mesh.MeshBuilder;
import de.coxcopi.util.math.Vector3;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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

        quad.transform.translate(new Vector3(0, 0, 0));

        quad.transform.scale(0.5);

        Mesh cube = MeshBuilder.createPrimitiveCuboid(1f, 1f, 1f);

        cube.transform.translate(new Vector3(3, -2, 0));

        Shader shader = ShaderParser.parseShaderFile("default.glsl");

        if (shader == null) {
            return;
        }

        Material material = new Material();
        quad.material = material;
        cube.material = material;

        renderer.addToRenderQueue(quad);
        renderer.addToRenderQueue(cube);

        Matrix4 mat1 = new Matrix4();
        mat1.setValue(0, 0, 4);
        mat1.setValue(1, 0, 2);
        mat1.setValue(1, 1, 8);
        mat1.setValue(2, 1, 1);
        mat1.setValue(1, 2, 1);

        Matrix4 mat2 = new Matrix4();
        mat2.setValue(0, 0, 4);
        mat2.setValue(1, 0, 2);
        mat2.setValue(2, 0, 1);
        mat2.setValue(0, 1, 2);
        mat2.setValue(2, 1, 4);
        mat2.setValue(0, 2, 9);
        mat2.setValue(1, 2, 4);
        mat2.setValue(2, 2, 2);

        mat1.multiply(mat2);

        while (!glfwWindowShouldClose(window)) {
            shader.bind();
            renderer.renderTick();
            shader.unbind();
        }
    }

    private void test() {

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        final float[] vertices = {
                -0.5f, -0.5f,
                0.0f,  0.5f,
                0.5f, -0.5f
        };

        final int[] indices = {
                0, 1, 2
        };

        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Vertex Buffer
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        // Bind Vertex Buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        // Index Buffer
        int ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Bind Index Buffer
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        final String vertexShaderSource = """
        #version 330 core
        layout(location = 0) in vec2 pos;
        out vec4 position;
        
        void main() {
            gl_Position = vec4(pos, 0.0, 1.0);
            position = gl_Position;
        }
        """;

        final String fragmentShaderSource = """
        #version 330 core
        out vec4 fragColor;
        in vec4 position;
        
        void main() {
            fragColor = vec4(position.x, position.y, 0.0, 1.0);
        }
        """;

        int shaderProgram = createShaderProgram(vertexShaderSource, fragmentShaderSource);
        GL20.glUseProgram(shaderProgram);

        // Unbind all Buffers, Programs and VAOs

        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // Set clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until user attempted to close
        // the window or pressed ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear framebuffer

            GL20.glUseProgram(shaderProgram);
            GL30.glBindVertexArray(vao);
            GL20.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

            GL20.glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            glfwSwapBuffers(window); // Swap buffers

            // Poll window events such as key callbacks
            glfwPollEvents();
        }
    }

    static int createShaderProgram(String vertexShader, String fragmentShader) {
        int shaderProgram = GL20.glCreateProgram();
        int vs = compileShader(GL20.GL_VERTEX_SHADER, vertexShader);
        int fs = compileShader(GL20.GL_FRAGMENT_SHADER, fragmentShader);
        
        GL20.glAttachShader(shaderProgram, vs);
        GL20.glAttachShader(shaderProgram, fs);
        GL20.glLinkProgram(shaderProgram);
        GL20.glValidateProgram(shaderProgram);
        
        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);
        
        return shaderProgram;
    }

    static int compileShader(int type, String source) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        int result = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (result == GL_FALSE) {
            System.out.println("Failed to compile " + (type == GL20.GL_VERTEX_SHADER ? "vertex" : "fragment") + " shader.");
            System.out.println(GL20.glGetShaderInfoLog(shader, GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH)));
            return 0;
        }
        return shader;
    }
}
