package net.vanernecomputing.validator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;

import net.vanernecomputing.validator.parser.XMLParser;
import net.vanernecomputing.validator.parser.CSVParser;

@SpringBootTest
public class ParserTests {
    @Test
    public void testXMLParser() throws FileNotFoundException, XMLStreamException {
        var records = XMLParser.parse("src/test/data/records.xml");
        // System.out.println(records);
        assertEquals(records.size(), 10, "The list of records has the same length as in the XML");
    }

    @Test
    public void textCSVParser() throws FileNotFoundException {
        var records = CSVParser.parse("src/test/data/records.csv");
        // System.out.println(records);
        assertEquals(records.size(), 10, "The list of records has the same length as in the CSV");
    }
}
