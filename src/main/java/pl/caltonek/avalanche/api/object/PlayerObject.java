package pl.caltonek.avalanche.api.object;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class PlayerObject {

    private final String name;
    private final UUID uuid;
    private final double x;
    private final double y;
    private final double z;
    private final float health;
    private final float maxHealth;
    private final int food;
    private final String gamemode;
    private final int ping;
    private final int entityId;
    private final boolean sneaking;
    private final boolean sprinting;
    private final boolean onGround;

    public PlayerObject(
            @NotNull final String name,
            @NotNull final UUID uuid,
            final double x,
            final double y,
            final double z,
            final float health,
            final float maxHealth,
            final int food,
            @NotNull final String gamemode,
            final int ping,
            final int entityId,
            final boolean sneaking,
            final boolean sprinting,
            final boolean onGround
    ) {
        this.name = name;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.health = health;
        this.maxHealth = maxHealth;
        this.food = food;
        this.gamemode = gamemode;
        this.ping = ping;
        this.entityId = entityId;
        this.sneaking = sneaking;
        this.sprinting = sprinting;
        this.onGround = onGround;
    }

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
    public int getEntityId() { return entityId; }

    public double getDistanceTo(@Nullable final PlayerObject other) {
        if (other == null) return -1.0;
        final double dx = this.x - other.x;
        final double dy = this.y - other.y;
        final double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public boolean isAlive() { return health > 0.0f; }
    public boolean isSneaking() { return sneaking; }
    public boolean isSprinting() { return sprinting; }
    public boolean isOnGround() { return onGround; }
}