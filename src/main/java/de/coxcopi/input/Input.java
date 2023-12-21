package de.coxcopi.input;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private static final ArrayList<Integer> press = new ArrayList<>();
    private static final ArrayList<Integer> pressed = new ArrayList<>();
    private static final ArrayList<Integer> released = new ArrayList<>();

    public static void processKeyCallback(long window, int key, int scancode, int action, int mods) {
        switch (action) {
            case GLFW_PRESS:
                setPressed(key);
                break;
            case GLFW_RELEASE:
                setReleased(key);
                break;
            case GLFW_REPEAT:
                break;
            default:
                // TODO: Log error (unknown key press action)
        }
    }

    public static void update() {
        pressed.addAll(press);
        press.clear();
        released.clear();
    }

    public static boolean isKeyPressed(int key) {
        return press.contains(key) || pressed.contains(key);
    }

    public static boolean isKeyJustPressed(int key) {
        return press.contains(key);
    }

    public static boolean isKeyJustReleased(int key) {
        return released.contains(key);
    }

    private static void setPressed(int key) {
        pressed.remove(Integer.valueOf(key));
        released.remove(Integer.valueOf(key));
        press.add(key);
    }

    private static void setReleased(int key) {
        press.remove(Integer.valueOf(key));
        pressed.remove(Integer.valueOf(key));
        if (!released.contains(key)) {
            released.add(key);
        }
    }

}
