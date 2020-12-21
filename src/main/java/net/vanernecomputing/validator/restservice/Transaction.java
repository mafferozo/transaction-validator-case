package net.vanernecomputing.validator.restservice;

import java.util.List;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class Transaction extends RepresentationModel<Transaction> {
    private final int id;
    private final List<Record> records;

    Transaction(int id, List<Record> records) {
        this.id = id;
        this.records = Objects.requireNonNull(records, "records can not be null");
    }

    public int getId() {
        return Objects.hash(this.id);
    }

    public List<Record> getRecords() {
        return this.records;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
