package pl.caltonek.avalanche.api.event.input.mouse;

import pl.caltonek.avalanche.api.event.CancellableEvent;

public final class MouseClickEvent extends CancellableEvent {
    private final int button;
    private final double x;
    private final double y;
    private final int modifiers;

    public MouseClickEvent(int button, double x, double y, int modifiers) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.modifiers = modifiers;
    }

    public int getButton() { return button; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getModifiers() { return modifiers; }
}