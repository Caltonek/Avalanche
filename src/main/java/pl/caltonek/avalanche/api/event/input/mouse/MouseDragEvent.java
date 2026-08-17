package pl.caltonek.avalanche.api.event.input.mouse;

import pl.caltonek.avalanche.api.event.CancellableEvent;

public final class MouseDragEvent extends CancellableEvent {
    private final int button;
    private final double x;
    private final double y;
    private final double deltaX;
    private final double deltaY;

    public MouseDragEvent(int button, double x, double y, double deltaX, double deltaY) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getButton() { return button; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getDeltaX() { return deltaX; }
    public double getDeltaY() { return deltaY; }
}