package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.PlayerObject;

import java.util.List;

public interface MultiplayerService {
    boolean isConnected();
    @Nullable String getServerAddress();
    @NotNull List<PlayerObject> getPlayers();
    @Nullable PlayerObject getPlayer(@NotNull final String name);
    int getPlayerCount();
}