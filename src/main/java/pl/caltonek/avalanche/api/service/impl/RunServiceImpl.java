package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.service.RunService;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public final class RunServiceImpl implements RunService {

    public final LuaSignal RenderStepped = new LuaSignal("minecraft.lifecycle.onrender");
    public final LuaSignal Tick = new LuaSignal("minecraft.lifecycle.ontick");
    public final LuaSignal Stepped = new LuaSignal("minecraft.lifecycle.onclienttick");
    public final LuaSignal RenderWorld = new LuaSignal("minecraft.lifecycle.onrenderworld");
    public final LuaSignal RenderHud = new LuaSignal("minecraft.lifecycle.onrenderhud");

    @Override
    public int getFps() {
        return GetFPS();
    }

    public int GetFPS() {
        return MinecraftClient.getInstance().getCurrentFps();
    }

    @Override @NotNull public LuaSignal getRenderStepped() { return RenderStepped; }
    @Override @NotNull public LuaSignal getTick() { return Tick; }
    @Override @NotNull public LuaSignal getStepped() { return Stepped; }
    @Override @NotNull public LuaSignal getRenderWorld() { return RenderWorld; }
    @Override @NotNull public LuaSignal getRenderHud() { return RenderHud; }
}