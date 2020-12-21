package net.vanernecomputing.validator.restservice;

public class TransactionNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TransactionNotFoundException(int id) {
        super("Could not find report" + id);
    }
}
