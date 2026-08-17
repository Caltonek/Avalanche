package pl.caltonek.avalanche.api.event;

import org.jetbrains.annotations.NotNull;

public final class ChatReceiveEvent extends CancellableEvent {

    public String message;
    public final String sender;

    public ChatReceiveEvent(@NotNull final String message, @NotNull final String sender) {
        this.message = message;
        this.sender = sender;
    }

    @NotNull public String getMessage() { return message; }
    @NotNull public String getSender() { return sender; }
    public void setMessage(@NotNull final String message) { this.message = message; }
}