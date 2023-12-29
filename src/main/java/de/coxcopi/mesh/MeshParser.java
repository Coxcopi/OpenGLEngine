package de.coxcopi.mesh;

import de.coxcopi.util.math.Vector3;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MeshParser {

    final static String DEFAULT_MESH_PATH = "meshes/";
    private enum ObjLineType {
        UNDEFINED,
        VERTEX,
        VERTEX_TEXTURE_COORDS,
        VERTEX_NORMALS,
        VERTEX_PARAM,
        FACE,
        SHADING_TYPE,
        MESH_NAME
    }

    static class VertexData {

        // Number of float values one vertex is made up of
        public static final int ELEM_COUNT = 6;

        // Position
        final float x;
        final float y;
        final float z;

        // Normal
        public float nX = 0;
        public float nY = 0;
        public float nZ = 0;

        // Texture Coordinates
        public float tU = 0;
        public float tV = 0;

        public VertexData(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static Mesh parseOBJ(String filename) {
        Path path;
        try {
            ClassLoader classLoader = MeshParser.class.getClassLoader();
            final String filePath = DEFAULT_MESH_PATH + filename + (filename.endsWith(".obj") ? "" : ".obj");
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
        try {
            final List<String> source = Files.readAllLines(path, StandardCharsets.UTF_8);
            final ArrayList<Vector3> vertexPositions = new ArrayList<>();
            final ArrayList<Vector3> vertexNormals = new ArrayList<>();
            final ArrayList<Vector3> textureCoords = new ArrayList<>(); // TODO: Implement
            final ArrayList<Integer> indices = new ArrayList<>();
            final ArrayList<VertexData> vertexData = new ArrayList<>();
            int lineNumber = 0;
            for (String line : source) {
                lineNumber++;
                String[] sections = line.split(" ");
                // Skip lines with an invalid amount of sections
                if (sections[0].equals("#")) {
                    System.out.println("Line contains comment, skipping: '" + line + "' [" + lineNumber + "].");
                    continue;
                }
                final ObjLineType lineType = objLineTypeFromPrefix(sections[0]);
                // Skip lines with an invalid prefix
                if (lineType == ObjLineType.UNDEFINED) {
                    System.out.println("Invalid prefix: '" + sections[0] + "' [" + lineNumber + "].");
                }
                switch (lineType) {
                    case VERTEX:
                        // Ignore 1- and 2-dimensional vertex coordinates
                        if (sections.length - 1 < 3) {
                            continue;
                        }
                        vertexPositions.add(new Vector3(
                                stringToFloat(sections[1]),
                                stringToFloat(sections[2]),
                                stringToFloat(sections[3])
                        ));
                        vertexData.add(new VertexData(
                                stringToFloat(sections[1]),
                                stringToFloat(sections[2]),
                                stringToFloat(sections[3])
                        ));
                        break;
                    case VERTEX_TEXTURE_COORDS:
                        // TODO: We should really implement a Vector2 class at some point
                        textureCoords.add(new Vector3(
                                stringToFloat(sections[1]),
                                stringToFloat(sections[2]),
                                0
                        ));
                        if (sections.length >= 4) {
                            System.out.println("Detected texture coordinates with 3 or more dimensions. Note that the obj parser only supports 2-dimensional texture coordinates. [" + lineNumber + "].");
                        }
                        break;
                    case VERTEX_NORMALS:
                        // Ignore 1- and 2-dimensional vertex normals
                        if (sections.length - 1 < 3) {
                            continue;
                        }
                        vertexNormals.add(new Vector3(
                                stringToFloat(sections[1]),
                                stringToFloat(sections[2]),
                                stringToFloat(sections[3])
                        ));
                        break;
                    case VERTEX_PARAM:
                        System.out.println("Skipping parameter space vertex. [" + lineNumber + "].");
                        break;
                    case FACE:
                        if (sections.length - 1 != 3) {
                            System.out.println("Face is defined by less or more than 3 vertices. Note that the obj parser currently does not support any n-gons. [" + lineNumber + "].");
                            continue;
                        }
                        for (int i = 1; i < 4; i++) {
                            String[] vert = sections[i].split("/");
                            // If a texture coordinate index is supplied for the current vertex
                            if (!vert[1].isEmpty()) {
                                // TODO: Implement texture coordinates
                            }
                            float[] vertNormals = vertexNormals.get(stringToInteger(vert[2]) - 1).toFloatArray();
                            VertexData vd = vertexData.get(stringToInteger(vert[0]) - 1);
                            vd.nX = vertNormals[0];
                            vd.nY = vertNormals[1];
                            vd.nZ = vertNormals[2];

                            indices.add(stringToInteger(vert[0]) - 1);
                        }
                        break;
                    case SHADING_TYPE:
                        final boolean shadeSmooth = sections[1].equals("1");
                        if (!shadeSmooth) {
                            System.out.println("Flat shading detected. Note that the OBJ parser currently only supports smooth-shaded meshes. [" + lineNumber + "].");
                        }
                        break;
                    case MESH_NAME:
                        // TODO: Implement object / mesh names (?)
                        System.out.println("Skipping object name definition: " + sections[1] + ". NOTE: Object names are not implemented. [" + lineNumber + "].");
                    default:
                        break;
                }
            }
            return MeshBuilder.createMesh(vertexDataArrayListToArray(vertexData), intArrayListToArray(indices));
        } catch (IOException exception) {
            exception.printStackTrace();
            // TODO: Error message
            return null;
        }
    }

    private static ObjLineType objLineTypeFromPrefix(String prefix) {
        return switch (prefix) {
            case "v" -> ObjLineType.VERTEX;
            case "vt" -> ObjLineType.VERTEX_TEXTURE_COORDS;
            case "vn" -> ObjLineType.VERTEX_NORMALS;
            case "vp" -> ObjLineType.VERTEX_PARAM;
            case "f" -> ObjLineType.FACE;
            case "s" -> ObjLineType.SHADING_TYPE;
            case "o" -> ObjLineType.MESH_NAME;
            default -> ObjLineType.UNDEFINED;
        };
    }

    private static Float stringToFloat(@NotNull String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return 0f;
        }
    }

    private static Integer stringToInteger(@NotNull String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    private static float[] floatArrayListToArray(@NotNull ArrayList<Float> arrayList) {
        final float[] array = new float[arrayList.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private static float[] vertexDataArrayListToArray(@NotNull ArrayList<VertexData> arrayList) {
        final ArrayList<Float> vdf = new ArrayList<>();
        for (VertexData vd : arrayList) {
            vdf.add(vd.x);
            vdf.add(vd.y);
            vdf.add(vd.z);
            vdf.add(vd.nX);
            vdf.add(vd.nY);
            vdf.add(vd.nZ);
        }
        return floatArrayListToArray(vdf);
    }

    private static int[] intArrayListToArray(@NotNull ArrayList<Integer> arrayList) {
        final int[] array = new int[arrayList.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private static void printVertexArray(float[] verts) {
        for (int y = 0; y < verts.length / 6; y++) {
            for (int x = 0; x < 6; x++) {
                if (x == 3) {
                    System.out.print(" - ");
                }
                System.out.print(verts[y + x] + ",");
            }
            System.out.println();
        }
    }
}
