package pl.caltonek.avalanche.api.object;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record PlayerObject(
        @NotNull String name,
        @NotNull UUID uuid,
        double x,
        double y,
        double z,
        float health,
        float maxHealth,
        int food,
        @NotNull String gamemode,
        int ping,
        int entityId,
        boolean sneaking,
        boolean sprinting,
        boolean onGround
) {
    public double getDistanceTo(@Nullable final PlayerObject other) {
        if (other == null) return -1.0;
        final double dx = this.x - other.x;
        final double dy = this.y - other.y;
        final double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public boolean isAlive() { return health > 0.0f; }

    @NotNull public String getName() { return name; }
    @NotNull public String getUuid() { return uuid.toString(); }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public int getFood() { return food; }
    @NotNull public String getGamemode() { return gamemode; }
    public int getPing() { return ping; }
    public int getLatency() { return ping; }
    public int getEntityId() { return entityId; }
    public boolean isSneaking() { return sneaking; }
    public boolean isSprinting() { return sprinting; }
    public boolean isOnGround() { return onGround; }
}