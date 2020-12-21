package net.vanernecomputing.validator.restservice;

import java.util.Objects;

public class FailedRecord {

    private final int reference;
    private final int error_code;
    private final String description;

    protected FailedRecord(int reference, int error_code, String description) {
        this.reference = reference;
        this.error_code = error_code;
        this.description = Objects.requireNonNull(description, "Description can not be null.");
    }

    public int getReference() {
        return reference;
    }

    public int getErrorCode() {
        return error_code;
    }

    public String getDescription() {
        return description;
    }

    public static FailedRecord notUnique(int reference) {
        return new FailedRecord(reference, 0, "Reference is not unique.");
    }

    public static FailedRecord incorrectEndBalance(int reference) {
        return new FailedRecord(reference, 1, "Incorrect end balance.");
    }
}
