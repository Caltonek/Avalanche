package pl.caltonek.avalanche.api.event;

import org.jetbrains.annotations.NotNull;

public final class ChatSendEvent extends CancellableEvent {

    private String message;

    public ChatSendEvent(@NotNull final String message) {
        this.message = message;
    }

    @NotNull public String getMessage() { return message; }
    public void setMessage(@NotNull final String message) { this.message = message; }
}