package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.service.UserInputService;
import pl.caltonek.avalanche.api.signal.LuaSignal;
import pl.caltonek.avalanche.util.KeyMap;

public final class UserInputServiceImpl implements UserInputService {

    public final LuaSignal InputBegan = new LuaSignal("minecraft.keyboard.onkeypress");
    public final LuaSignal InputEnded = new LuaSignal("minecraft.keyboard.onkeyrelease");
    public final LuaSignal KeyRepeat = new LuaSignal("minecraft.keyboard.onkeyrepeat");
    public final LuaSignal CharTyped = new LuaSignal("minecraft.keyboard.onchartyped");
    public final LuaSignal MouseButton1Down = new LuaSignal("minecraft.mouse.onmousepress");
    public final LuaSignal MouseButton1Up = new LuaSignal("minecraft.mouse.onmouserelease");
    public final LuaSignal MouseMoved = new LuaSignal("minecraft.mouse.onmousemove");
    public final LuaSignal MouseScrolled = new LuaSignal("minecraft.mouse.onmousescroll");

    @Override
    public boolean isKeyDown(int keyCode) {
        return IsKeyDown(keyCode);
    }

    public boolean IsKeyDown(int keyCode) {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        return InputUtil.isKeyPressed(handle, keyCode);
    }

    @Override
    public boolean isKeyDown(@NotNull String keyName) {
        return IsKeyDown(keyName);
    }

    public boolean IsKeyDown(String keyName) {
        int code = KeyMap.getKeyCode(keyName);
        return code != -1 && IsKeyDown(code);
    }

    @Override
    public boolean isMouseButtonDown(int button) {
        return IsMouseButtonDown(button);
    }

    public boolean IsMouseButtonDown(int button) {
        var mouse = MinecraftClient.getInstance().mouse;
        return (button == 0 && mouse.wasLeftButtonClicked()) || (button == 1 && mouse.wasRightButtonClicked());
    }

    @Override
    public double[] getMouseLocation() {
        return GetMouseLocation();
    }

    public double[] GetMouseLocation() {
        var mouse = MinecraftClient.getInstance().mouse;
        return new double[]{ mouse.getX(), mouse.getY() };
    }

    @Override
    public double[] getScaledMouseLocation() {
        return GetScaledMouseLocation();
    }

    public double[] GetScaledMouseLocation() {
        var client = MinecraftClient.getInstance();
        double scaledX = client.mouse.getX() * (double) client.getWindow().getScaledWidth() / (double) client.getWindow().getWidth();
        double scaledY = client.mouse.getY() * (double) client.getWindow().getScaledHeight() / (double) client.getWindow().getHeight();
        return new double[]{ scaledX, scaledY };
    }

    @Override @NotNull public LuaSignal getInputBegan() { return InputBegan; }
    @Override @NotNull public LuaSignal getInputEnded() { return InputEnded; }
    @Override @NotNull public LuaSignal getKeyRepeat() { return KeyRepeat; }
    @Override @NotNull public LuaSignal getCharTyped() { return CharTyped; }
    @Override @NotNull public LuaSignal getMouseButton1Down() { return MouseButton1Down; }
    @Override @NotNull public LuaSignal getMouseButton1Up() { return MouseButton1Up; }
    @Override @NotNull public LuaSignal getMouseMoved() { return MouseMoved; }
    @Override @NotNull public LuaSignal getMouseScrolled() { return MouseScrolled; }
}