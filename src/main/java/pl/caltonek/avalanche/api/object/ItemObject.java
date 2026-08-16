package pl.caltonek.avalanche.api.object;

import org.jetbrains.annotations.NotNull;

public record ItemObject(String id, int count, int maxCount, int slot, String name, int damage, int maxDamage) {

    public ItemObject(
            @NotNull final String id,
            final int count,
            final int maxCount,
            final int slot,
            @NotNull final String name,
            final int damage,
            final int maxDamage
    ) {
        this.id = id;
        this.count = count;
        this.maxCount = maxCount;
        this.slot = slot;
        this.name = name;
        this.damage = damage;
        this.maxDamage = maxDamage;
    }

    @Override
    @NotNull
    public String id() {
        return id;
    }

    @Override
    @NotNull
    public String name() {
        return name;
    }

    public boolean isEmpty() {
        return id.equals("minecraft:air") || count <= 0;
    }
}