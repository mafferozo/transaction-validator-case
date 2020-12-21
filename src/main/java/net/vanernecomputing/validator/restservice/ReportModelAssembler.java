package net.vanernecomputing.validator.restservice;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReportModelAssembler implements RepresentationModelAssembler<Report, EntityModel<Report>> {

    @Override
    public EntityModel<Report> toModel(Report entity) {
        return EntityModel.of(entity, //
                linkTo(methodOn(ValidatorController.class).oneReport(entity.getId())).withSelfRel(),
                linkTo(methodOn(ValidatorController.class).oneTransaction(entity.getId())).withRel("transaction"),
                linkTo(methodOn(ValidatorController.class).allReports()).withRel("reports"));
    }

}
