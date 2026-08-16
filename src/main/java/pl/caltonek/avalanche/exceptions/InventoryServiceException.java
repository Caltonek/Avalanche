package pl.caltonek.avalanche.exceptions;

public final class InventoryServiceException extends ServiceException {

    public InventoryServiceException(final String message) {
        super(message);
    }

    public InventoryServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}