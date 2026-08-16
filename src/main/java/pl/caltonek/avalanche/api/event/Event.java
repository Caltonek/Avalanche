package pl.caltonek.avalanche.api.event;

public abstract class Event {
    private final long timestamp = System.currentTimeMillis();
    public long getTimestamp() { return timestamp; }
}