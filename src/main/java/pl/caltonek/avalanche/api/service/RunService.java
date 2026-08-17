package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public interface RunService {

    int getFps();

    @NotNull LuaSignal getRenderStepped();
    @NotNull LuaSignal getTick();
    @NotNull LuaSignal getStepped();
    @NotNull LuaSignal getRenderWorld();
    @NotNull LuaSignal getRenderHud();
}