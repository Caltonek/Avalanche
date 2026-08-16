package pl.caltonek.avalanche.api.event;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.object.PlayerObject;

public final class PlayerJoinEvent extends Event {

    private final PlayerObject player;

    public PlayerJoinEvent(@NotNull final PlayerObject player) {
        this.player = player;
    }

    @NotNull public PlayerObject getPlayer() { return player; }
}