package pl.caltonek.avalanche.api.event;

public abstract class CancellableEvent extends Event {
    private boolean cancelled = false;

    public boolean isCancelled() { return cancelled; }
    public void cancel() { this.cancelled = true; }
    public void setCancelled(final boolean cancelled) { this.cancelled = cancelled; }
}