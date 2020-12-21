package net.vanernecomputing.validator.restservice;

import java.math.BigDecimal;
import java.util.Objects;

public class Record {
    public static class Builder {
        private int reference;
        private String accountNumber;
        private String description;
        private BigDecimal startBalance;
        private BigDecimal endBalance;
        private BigDecimal mutation;

        public Builder(int reference) {
            this.reference = reference;
        }

        public Builder() {
        }

        public Builder withReference(int reference) {
            this.reference = reference;
            return this;
        }

        public Builder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withStartBalance(BigDecimal startBalance) {
            this.startBalance = startBalance;
            return this;
        }

        public Builder withEndBalance(BigDecimal endBalance) {
            this.endBalance = endBalance;
            return this;
        }

        public Builder withMutation(BigDecimal mutation) {
            this.mutation = mutation;
            return this;
        }

        public Record build() {
            return new Record(this.reference, this.accountNumber, this.description, this.startBalance, this.endBalance,
                    this.mutation);
        }

        @Override
        public String toString() {
            return String.format(
                    "Builder:\n\treference: \t%s\n\tdescription: \t%s\n\tstartbalance: \t%s\n\tmutation: \t%s\n\tendbalance: \t%s\n\t",
                    this.reference, this.description, this.startBalance, this.mutation, this.endBalance);
        }
    }

    private final int reference;
    private final String accountNumber;
    private final String description;
    private final BigDecimal startBalance;
    private final BigDecimal endBalance;
    private final BigDecimal mutation;

    public Record(int reference, String accountNumber, String description, BigDecimal startBalance,
            BigDecimal endBalance, BigDecimal mutation) {
        this.reference = reference;
        this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber can not be null");
        this.description = Objects.requireNonNull(description, "description can not be null");
        this.startBalance = Objects.requireNonNull(startBalance, "startBalance can not be null");
        this.endBalance = Objects.requireNonNull(endBalance, "endBalance can not be null");
        this.mutation = Objects.requireNonNull(mutation, "mutation can not be null");
    }

    public int getReference() {
        return this.reference;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public BigDecimal getStartBalance() {
        return this.startBalance;
    }

    public BigDecimal getEndBalance() {
        return this.endBalance;
    }

    public BigDecimal getMutation() {
        return this.mutation;
    }

    @Override
    public String toString() {
        return String.format(
                "\nRecord:\n\treference: \t%s\n\tdescription: \t%s\n\tstartbalance: \t%s\n\tmutation: \t%s\n\tendbalance: \t%s\n\t",
                this.reference, this.description, this.startBalance, this.mutation, this.endBalance);
    }
}
