package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.PlayerObject;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public interface PlayerService {

    @Nullable PlayerObject getLocal();

    LuaSignal getSpawned();
    LuaSignal getDied();
    LuaSignal getMoved();
    LuaSignal getJumped();
    LuaSignal getAttacked();
    LuaSignal getInteracted();
}