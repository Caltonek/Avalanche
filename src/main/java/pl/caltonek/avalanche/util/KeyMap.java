package pl.caltonek.avalanche.util;

import org.lwjgl.glfw.GLFW;
import java.util.HashMap;
import java.util.Map;

public class KeyMap {
    private static final Map<Integer, String> CODE_TO_NAME = new HashMap<>();
    private static final Map<String, Integer> NAME_TO_CODE = new HashMap<>();

    static {
        register(GLFW.GLFW_KEY_SPACE, "SPACE");
        register(GLFW.GLFW_KEY_APOSTROPHE, "APOSTROPHE");
        register(GLFW.GLFW_KEY_COMMA, "COMMA");
        register(GLFW.GLFW_KEY_MINUS, "MINUS");
        register(GLFW.GLFW_KEY_PERIOD, "PERIOD");
        register(GLFW.GLFW_KEY_SLASH, "SLASH");
        register(GLFW.GLFW_KEY_SEMICOLON, "SEMICOLON");
        register(GLFW.GLFW_KEY_EQUAL, "EQUAL");
        register(GLFW.GLFW_KEY_LEFT_BRACKET, "LEFT_BRACKET");
        register(GLFW.GLFW_KEY_BACKSLASH, "BACKSLASH");
        register(GLFW.GLFW_KEY_RIGHT_BRACKET, "RIGHT_BRACKET");
        register(GLFW.GLFW_KEY_GRAVE_ACCENT, "GRAVE_ACCENT");
        register(GLFW.GLFW_KEY_WORLD_1, "WORLD_1");
        register(GLFW.GLFW_KEY_WORLD_2, "WORLD_2");

        for (int i = 0; i <= 9; i++) {
            register(GLFW.GLFW_KEY_0 + i, String.valueOf(i));
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            register(GLFW.GLFW_KEY_A + (c - 'A'), String.valueOf(c));
        }

        register(GLFW.GLFW_KEY_ESCAPE, "ESCAPE");
        register(GLFW.GLFW_KEY_ENTER, "ENTER");
        register(GLFW.GLFW_KEY_TAB, "TAB");
        register(GLFW.GLFW_KEY_BACKSPACE, "BACKSPACE");
        register(GLFW.GLFW_KEY_INSERT, "INSERT");
        register(GLFW.GLFW_KEY_DELETE, "DELETE");
        register(GLFW.GLFW_KEY_RIGHT, "RIGHT");
        register(GLFW.GLFW_KEY_LEFT, "LEFT");
        register(GLFW.GLFW_KEY_DOWN, "DOWN");
        register(GLFW.GLFW_KEY_UP, "UP");
        register(GLFW.GLFW_KEY_PAGE_UP, "PAGE_UP");
        register(GLFW.GLFW_KEY_PAGE_DOWN, "PAGE_DOWN");
        register(GLFW.GLFW_KEY_HOME, "HOME");
        register(GLFW.GLFW_KEY_END, "END");
        register(GLFW.GLFW_KEY_CAPS_LOCK, "CAPS_LOCK");
        register(GLFW.GLFW_KEY_SCROLL_LOCK, "SCROLL_LOCK");
        register(GLFW.GLFW_KEY_NUM_LOCK, "NUM_LOCK");
        register(GLFW.GLFW_KEY_PRINT_SCREEN, "PRINT_SCREEN");
        register(GLFW.GLFW_KEY_PAUSE, "PAUSE");

        for (int i = 1; i <= 25; i++) {
            register(GLFW.GLFW_KEY_F1 + (i - 1), "F" + i);
        }

        for (int i = 0; i <= 9; i++) {
            register(GLFW.GLFW_KEY_KP_0 + i, "KP_" + i);
            NAME_TO_CODE.put("NUMPAD_" + i, GLFW.GLFW_KEY_KP_0 + i);
        }
        register(GLFW.GLFW_KEY_KP_DECIMAL, "KP_DECIMAL");
        register(GLFW.GLFW_KEY_KP_DIVIDE, "KP_DIVIDE");
        register(GLFW.GLFW_KEY_KP_MULTIPLY, "KP_MULTIPLY");
        register(GLFW.GLFW_KEY_KP_SUBTRACT, "KP_SUBTRACT");
        register(GLFW.GLFW_KEY_KP_ADD, "KP_ADD");
        register(GLFW.GLFW_KEY_KP_ENTER, "KP_ENTER");
        register(GLFW.GLFW_KEY_KP_EQUAL, "KP_EQUAL");

        register(GLFW.GLFW_KEY_LEFT_SHIFT, "LEFT_SHIFT");
        register(GLFW.GLFW_KEY_LEFT_CONTROL, "LEFT_CONTROL");
        register(GLFW.GLFW_KEY_LEFT_ALT, "LEFT_ALT");
        register(GLFW.GLFW_KEY_LEFT_SUPER, "LEFT_SUPER");
        register(GLFW.GLFW_KEY_RIGHT_SHIFT, "RIGHT_SHIFT");
        register(GLFW.GLFW_KEY_RIGHT_CONTROL, "RIGHT_CONTROL");
        register(GLFW.GLFW_KEY_RIGHT_ALT, "RIGHT_ALT");
        register(GLFW.GLFW_KEY_RIGHT_SUPER, "RIGHT_SUPER");
        register(GLFW.GLFW_KEY_MENU, "MENU");

        NAME_TO_CODE.put("SHIFT", GLFW.GLFW_KEY_LEFT_SHIFT);
        NAME_TO_CODE.put("CTRL", GLFW.GLFW_KEY_LEFT_CONTROL);
        NAME_TO_CODE.put("CONTROL", GLFW.GLFW_KEY_LEFT_CONTROL);
        NAME_TO_CODE.put("ALT", GLFW.GLFW_KEY_LEFT_ALT);
        NAME_TO_CODE.put("RETURN", GLFW.GLFW_KEY_ENTER);
        NAME_TO_CODE.put("TILDE", GLFW.GLFW_KEY_GRAVE_ACCENT);
    }

    private static void register(int code, String name) {
        CODE_TO_NAME.put(code, name);
        NAME_TO_CODE.put(name.toUpperCase(), code);
    }

    public static String getKeyName(int code) {
        return CODE_TO_NAME.getOrDefault(code, "UNKNOWN");
    }

    public static int getKeyCode(String name) {
        if (name == null) return GLFW.GLFW_KEY_UNKNOWN;
        return NAME_TO_CODE.getOrDefault(name.toUpperCase(), GLFW.GLFW_KEY_UNKNOWN);
    }
}
