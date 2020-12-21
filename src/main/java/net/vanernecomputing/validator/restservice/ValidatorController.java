package net.vanernecomputing.validator.restservice;

import net.vanernecomputing.validator.parser.CSVParser;
import net.vanernecomputing.validator.parser.XMLParser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.stream.XMLStreamException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.io.InputStream;

@RestController
public class ValidatorController {
    private final AtomicInteger idCounter = new AtomicInteger();
    private final ConcurrentHashMap<Integer, Transaction> transactionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Report> reportMap = new ConcurrentHashMap<>();
    private final TransactionModelAssembler tAssembler;
    private final ReportModelAssembler rAssembler;

    ValidatorController(TransactionModelAssembler tAssembler, ReportModelAssembler rAssembler) {
        this.tAssembler = tAssembler;
        this.rAssembler = rAssembler;
    }

    @PostMapping(value = "/transactions", consumes = "text/csv")
    public ResponseEntity<?> acceptCSV(InputStream data) {

        // Todo: Handle incorrect CSV's (now, the application just breaks..)
        List<Record> records = CSVParser.parse(data);

        return this.createTransaction(records);
    }

    @PostMapping(value = "/transactions", consumes = "text/xml")
    public ResponseEntity<?> acceptXML(InputStream data) {
        List<Record> records;

        try {
            records = XMLParser.parse(data);
        } catch (XMLStreamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return this.createTransaction(records);
    }

    private ResponseEntity<?> createTransaction(List<Record> records) {
        Transaction transaction = new Transaction(idCounter.incrementAndGet(), records);
        Report report = Report.validate(transaction);
        transactionMap.put(transaction.getId(), transaction);
        reportMap.put(report.getId(), report);

        EntityModel<Transaction> model = tAssembler.toModel(transaction);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/transactions")
    public CollectionModel<EntityModel<Transaction>> allTransactions() {
        List<EntityModel<Transaction>> transactions = transactionMap.values().stream().map(tAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(transactions,
                linkTo(methodOn(ValidatorController.class).allTransactions()).withSelfRel());
    }

    @GetMapping("/transactions/{id}")
    public EntityModel<Transaction> oneTransaction(@PathVariable int id) {
        Transaction t = transactionMap.get(id);
        if (t == null) {
            throw new TransactionNotFoundException(id);
        }
        return tAssembler.toModel(t);
    }

    @GetMapping("/reports")
    public CollectionModel<EntityModel<Report>> allReports() {
        List<EntityModel<Report>> reports = reportMap.values().stream().map(rAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reports, linkTo(methodOn(ValidatorController.class).allReports()).withSelfRel());
    }

    @GetMapping("/reports/{id}")
    public EntityModel<Report> oneReport(@PathVariable int id) {
        Report r = reportMap.get(id);
        if (r == null) {
            throw new TransactionNotFoundException(id);
        }
        return rAssembler.toModel(r);
    }

}