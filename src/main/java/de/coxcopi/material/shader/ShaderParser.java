package de.coxcopi.material.shader;

import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;

public class ShaderParser {
    final static String DEFAULT_SHADER_PATH = "shaders/";

    public static Shader getDefaultShader() {
        return parseShaderFile("default.glsl");
    }

    /**
     * Loads a shader file and parses it as a Shader object.
     * Shader files are expected to be located inside the resources/shaders/ directory.
     * <p>
     * NOTE: Shader files require the '.glsl' filetype in order for the parser to work. If a file could
     * not be found, the method returns null and an error is logged.
     * @param name The name of the shader file. Including the '.glsl' suffix in the name is optional.
     * @return A shader object containing the source of both shader types and a valid shaderProgram id.
     */
    public static Shader parseShaderFile(String name) {
        Path path;
        try {
            ClassLoader classLoader = ShaderParser.class.getClassLoader();
            final String filePath = DEFAULT_SHADER_PATH + name + (name.endsWith(".glsl") ? "" : ".glsl");
            URL url = classLoader.getResource(filePath);
            if (url == null) {
                // TODO: Error message (file not found)
                System.out.println("File not found.");
                return null;
            }
            URI uri = url.toURI();
            path = Paths.get(uri);
        } catch (URISyntaxException e) {
            // TODO: Error message (Invalid uri syntax / Internal Parse Error)
            return null;
        }
        enum ShaderType {
            NONE,
            VERTEX,
            FRAGMENT
        }
        ShaderType type = ShaderType.NONE;
        try {
            final List<String> sourceLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String vertexSource = "";
            String fragmentSource = "";
            for (String line : sourceLines) {
                if (line.contains("#shader")) {
                    if (line.contains("fragment")) {
                        type = ShaderType.FRAGMENT;
                    }
                    else if (line.contains("vertex")) {
                        type = ShaderType.VERTEX;
                    }
                }
                else {
                    if (type == ShaderType.FRAGMENT) {
                        fragmentSource += line + "\n";
                    } else if (type == ShaderType.VERTEX) {
                        vertexSource += line + "\n";
                    }
                }
            }
            final int programID = createShaderProgram(vertexSource, fragmentSource);
            final ShaderProgramSource source = new ShaderProgramSource(vertexSource, fragmentSource);
            return new Shader(source, programID);
        } catch (IOException e) {
            System.out.println("Error loading shader file at " + path + ":");
            e.printStackTrace();
            return null;
        }
    }

    private static int createShaderProgram(String vertexSource, String fragmentSource) {
        final int shaderProgram = GL20.glCreateProgram();
        final int vs = compileShader(GL20.GL_VERTEX_SHADER, vertexSource);
        final int fs = compileShader(GL20.GL_FRAGMENT_SHADER, fragmentSource);

        GL20.glAttachShader(shaderProgram, vs);
        GL20.glAttachShader(shaderProgram, fs);
        GL20.glLinkProgram(shaderProgram);
        GL20.glValidateProgram(shaderProgram);

        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);

        return shaderProgram;
    }

    private static int compileShader(int type, String source) {
        final int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);
        final int result = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (result == GL_FALSE) {
            System.out.println("Failed to compile " + (type == GL20.GL_VERTEX_SHADER ? "vertex" : "fragment") + " shader:");
            System.out.println(GL20.glGetShaderInfoLog(shader, GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH)));
            return 0;
        }
        return shader;
    }
}
