package ogorek.wojciech.persistance.exception;

public class JsonAppException extends RuntimeException {

    private final String message;

    public JsonAppException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

