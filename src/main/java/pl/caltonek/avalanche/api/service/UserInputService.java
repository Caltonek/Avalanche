package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public interface UserInputService {
    boolean isKeyDown(int keyCode);
    boolean isKeyDown(@NotNull String keyName);
    boolean isMouseButtonDown(int button);
    double[] getMouseLocation();
    double[] getScaledMouseLocation();

    @NotNull LuaSignal getInputBegan();
    @NotNull LuaSignal getInputEnded();
    @NotNull LuaSignal getKeyRepeat();
    @NotNull LuaSignal getCharTyped();
    @NotNull LuaSignal getMouseButton1Down();
    @NotNull LuaSignal getMouseButton1Up();
    @NotNull LuaSignal getMouseMoved();
    @NotNull LuaSignal getMouseScrolled();
}