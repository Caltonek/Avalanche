package pl.caltonek.avalanche.api.event.input.keyboard;

import pl.caltonek.avalanche.api.event.CancellableEvent;

public final class CharTypedEvent extends CancellableEvent {
    private final int codePoint;
    private final char character;
    private final int modifiers;

    public CharTypedEvent(int codePoint, int modifiers) {
        this.codePoint = codePoint;
        this.character = (char) codePoint;
        this.modifiers = modifiers;
    }

    public int getCodePoint() { return codePoint; }
    public char getCharacter() { return character; }
    public int getModifiers() { return modifiers; }
}