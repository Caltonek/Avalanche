package pl.caltonek.avalanche.api.event.input.mouse;

import pl.caltonek.avalanche.api.event.CancellableEvent;

public final class MouseScrollEvent extends CancellableEvent {
    private final double horizontal;
    private final double vertical;

    public MouseScrollEvent(double horizontal, double vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public double getHorizontal() { return horizontal; }
    public double getVertical() { return vertical; }
}