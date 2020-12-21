package net.vanernecomputing.validator.restservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

public class Report extends RepresentationModel<Report> {
    private final List<FailedRecord> failedRecords;
    private final int id;

    Report(int id, List<FailedRecord> failedRecords) {
        this.id = id;
        this.failedRecords = failedRecords;
    }

    public int getId() {
        return this.id;
    }

    public List<FailedRecord> getFailedRecords() {
        return this.failedRecords;
    }

    public static Report validate(Transaction transaction) {
        final HashSet<Integer> references = new HashSet<>();
        final List<FailedRecord> failedRecords = new ArrayList<>();

        for (Record record : transaction.getRecords()) {
            int ref = record.getReference();
            if (!references.add(ref)) {
                failedRecords.add(FailedRecord.notUnique(ref));
            }

            BigDecimal computed = record.getStartBalance().add(record.getMutation());

            // equals vs compareTo:
            // https://stackoverflow.com/questions/6787142/bigdecimal-equals-versus-compareto
            if (!computed.equals(record.getEndBalance())) {
                failedRecords.add(FailedRecord.incorrectEndBalance(record.getReference()));
            }
        }

        return new Report(transaction.getId(), failedRecords);
    }

}
