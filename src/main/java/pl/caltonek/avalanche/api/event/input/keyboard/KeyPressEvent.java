package pl.caltonek.avalanche.api.event.input.keyboard;

import pl.caltonek.avalanche.api.event.CancellableEvent;
import pl.caltonek.avalanche.util.KeyMap;

public final class KeyPressEvent extends CancellableEvent {
    private final int key;
    private final int scancode;
    private final int modifiers;

    public KeyPressEvent(int key, int scancode, int modifiers) {
        this.key = key;
        this.scancode = scancode;
        this.modifiers = modifiers;
    }

    public int getKey() { return key; }
    public String getKeyName() { return KeyMap.getKeyName(key); }
    public boolean isKey(String name) { return key == KeyMap.getKeyCode(name); }
    public int getScancode() { return scancode; }
    public int getModifiers() { return modifiers; }
}