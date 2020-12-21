package net.vanernecomputing.validator.parser;

import net.vanernecomputing.validator.restservice.Record;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.List;
import java.math.BigDecimal;

public class CSVParser {
    public static List<Record> parse(String filename) throws FileNotFoundException {
        return parse(new FileInputStream(filename));
    }

    public static List<Record> parse(InputStream stream) {
        List<Record> records = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().skip(1)
                .map(CSVParser::fromCSVLine).collect(Collectors.toList());
        return records;
    }

    private static Record fromCSVLine(String line) {
        String[] splits = line.split(",");
        Record.Builder b = new Record.Builder();
        b.withReference(Integer.parseInt(splits[0]));
        b.withAccountNumber(splits[1]);
        b.withDescription(splits[2]);
        b.withStartBalance(new BigDecimal(splits[3]));
        b.withMutation(new BigDecimal(splits[4]));
        b.withEndBalance(new BigDecimal(splits[5]));
        return b.build();
    }
}
