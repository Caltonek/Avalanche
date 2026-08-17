package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;

public interface CommandService {
    void register(@NotNull String name, @NotNull LuaValue executor);
    void register(@NotNull String name, @NotNull LuaValue executor, @NotNull LuaValue tabCompleter);
    void unregister(@NotNull String name);
    boolean isRegistered(@NotNull String name);

    void Register(@NotNull String name, @NotNull LuaValue executor);
    void Register(@NotNull String name, @NotNull LuaValue executor, @NotNull LuaValue tabCompleter);
    void Unregister(@NotNull String name);
    boolean IsRegistered(@NotNull String name);
}