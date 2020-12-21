package net.vanernecomputing.validator.restservice;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionModelAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

    @Override
    public EntityModel<Transaction> toModel(Transaction entity) {
        return EntityModel.of(entity, //
                linkTo(methodOn(ValidatorController.class).oneTransaction(entity.getId())).withSelfRel(),
                linkTo(methodOn(ValidatorController.class).oneReport(entity.getId())).withRel("report"),
                linkTo(methodOn(ValidatorController.class).allTransactions()).withRel("transactions"));
    }

}
