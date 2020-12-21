package net.vanernecomputing.validator.parser;

import net.vanernecomputing.validator.restservice.Record;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLParser {
    public static List<Record> parse(String filename) throws FileNotFoundException, XMLStreamException {
        return parse(new FileInputStream(filename));
    }

    public static List<Record> parse(InputStream stream) throws XMLStreamException {
        List<Record> recordList = new ArrayList<>();
        Record.Builder builder = new Record.Builder();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(stream);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                if (startElement.getName().getLocalPart().equals("record")) {
                    Attribute refAttribute = startElement.getAttributeByName(new QName("reference"));
                    int reference = Integer.parseInt(refAttribute.getValue());
                    builder.withReference(reference);

                } else if (startElement.getName().getLocalPart().equals("accountNumber")) {
                    String text = reader.nextEvent().asCharacters().getData();
                    builder.withAccountNumber(text);

                } else if (startElement.getName().getLocalPart().equals("description")) {
                    String text = reader.nextEvent().asCharacters().getData();
                    builder.withDescription(text);

                } else if (startElement.getName().getLocalPart().equals("startBalance")) {
                    String text = reader.nextEvent().asCharacters().getData();
                    BigDecimal startBalance = new BigDecimal(text);
                    builder.withStartBalance(startBalance);

                } else if (startElement.getName().getLocalPart().equals("mutation")) {
                    String text = reader.nextEvent().asCharacters().getData();
                    BigDecimal mutation = new BigDecimal(text);
                    builder.withMutation(mutation);

                } else if (startElement.getName().getLocalPart().equals("endBalance")) {
                    String text = reader.nextEvent().asCharacters().getData();
                    BigDecimal endBalance = new BigDecimal(text);
                    builder.withEndBalance(endBalance);

                }
            }
            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equals("record")) {
                    Record record = builder.build();
                    recordList.add(record);
                    builder = new Record.Builder();
                }
            }

        }
        return recordList;
    }
}