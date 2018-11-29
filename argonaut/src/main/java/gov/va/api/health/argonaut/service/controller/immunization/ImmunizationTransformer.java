package gov.va.api.health.argonaut.service.controller.immunization;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.DataAbsentReason;
import gov.va.api.health.argonaut.api.DataAbsentReason.Reason;
import gov.va.api.health.argonaut.api.datatypes.Annotation;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.datatypes.Identifier.IdentifierUse;
import gov.va.api.health.argonaut.api.elements.Extension;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Immunization;
import gov.va.api.health.argonaut.api.resources.Immunization.Reaction;
import gov.va.api.health.argonaut.api.resources.Immunization.Status;
import gov.va.api.health.argonaut.api.resources.Immunization.VaccinationProtocol;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwImmunization103Root.CdwImmunizations.CdwImmunization;
import gov.va.dvp.cdw.xsd.model.CdwImmunization103Root.CdwImmunizations.CdwImmunization.CdwIdentifiers;
import gov.va.dvp.cdw.xsd.model.CdwImmunization103Root.CdwImmunizations.CdwImmunization.CdwNotes;
import gov.va.dvp.cdw.xsd.model.CdwImmunization103Root.CdwImmunizations.CdwImmunization.CdwReactions;
import gov.va.dvp.cdw.xsd.model.CdwImmunization103Root.CdwImmunizations.CdwImmunization.CdwVaccinationProtocols;
import gov.va.dvp.cdw.xsd.model.CdwImmunizationReported;
import gov.va.dvp.cdw.xsd.model.CdwImmunizationStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImmunizationTransformer implements ImmunizationController.Transformer {

  @Override
  public Immunization apply(CdwImmunization source) {
    return Immunization.builder()
        .resourceType(Immunization.class.getSimpleName())
        .id(source.getCdwId())
        .identifier(identifier(source.getIdentifiers()))
        .status(status(source.getStatus()))
        ._status(statusExtension(source.getStatus()))
        .date(asDateTimeString(source.getDate()))
        .vaccineCode(vaccineCode(source.getVaccineCode()))
        .patient(reference(source.getPatient()))
        .wasNotGiven(source.isWasNotGiven())
        .reported(reported(source.getReported()))
        ._reported(reportedExtension(source.getReported()))
        .performer(reference(source.getPerformer()))
        .requester(reference(source.getRequester()))
        .encounter(reference(source.getEncounter()))
        .location(reference(source.getLocation()))
        .note(note(source.getNotes()))
        .reaction(reaction(source.getReactions()))
        .vaccinationProtocol(vaccinationProtocol(source.getVaccinationProtocols()))
        .build();
  }

  List<Coding> codings(List<CdwCoding> source) {
    return convertAll(
        source,
        item ->
            Coding.builder()
                .system(item.getSystem())
                .code(item.getCode())
                .display(item.getDisplay())
                .build());
  }

  List<Identifier> identifier(CdwIdentifiers maybeSource) {
    return convertAll(
        ifPresent(maybeSource, CdwIdentifiers::getIdentifier),
        source ->
            Identifier.builder()
                .system(source.getSystem())
                .value(source.getValue())
                .use(convert(source.getUse(), IdentifierUse::valueOf))
                .build());
  }

  List<Annotation> note(CdwNotes notes) {
    return convertAll(
        ifPresent(notes, CdwNotes::getNote),
        item -> Annotation.builder().text(item.getText()).build());
  }

  List<Reaction> reaction(CdwReactions maybeSource) {
    return convertAll(
        ifPresent(maybeSource, CdwReactions::getReaction),
        item -> Reaction.builder().detail(reference(item.getDetail())).build());
  }

  Reference reference(CdwReference maybeSource) {
    return convert(
        maybeSource,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  Boolean reported(CdwImmunizationReported source) {
    if (source == null || source == CdwImmunizationReported.DATA_ABSENT_REASON_UNSUPPORTED) {
      return null;
    }
    return source == CdwImmunizationReported.TRUE;
  }

  Extension reportedExtension(CdwImmunizationReported source) {
    if (source == null) {
      return DataAbsentReason.of(Reason.unknown);
    }
    if (source == CdwImmunizationReported.DATA_ABSENT_REASON_UNSUPPORTED) {
      return DataAbsentReason.of(Reason.unsupported);
    }
    return null;
  }

  Status status(CdwImmunizationStatus source) {
    if (source == null || source == CdwImmunizationStatus.DATA_ABSENT_REASON_UNSUPPORTED) {
      return null;
    }
    return EnumSearcher.of(Immunization.Status.class).find(source.value());
  }

  Extension statusExtension(CdwImmunizationStatus source) {
    if (source == null) {
      return DataAbsentReason.of(Reason.unknown);
    }
    if (source == CdwImmunizationStatus.DATA_ABSENT_REASON_UNSUPPORTED) {
      return DataAbsentReason.of(Reason.unsupported);
    }
    return null;
  }

  private List<VaccinationProtocol> vaccinationProtocol(CdwVaccinationProtocols maybeSource) {
    return convertAll(
        ifPresent(maybeSource, CdwVaccinationProtocols::getVaccinationProtocol),
        item ->
            VaccinationProtocol.builder()
                .series(item.getSeries())
                .seriesDoses(ifPresent(item.getSeriesDoses(), Short::intValue))
                .build());
  }

  private CodeableConcept vaccineCode(CdwCodeableConcept maybeSource) {
    return convert(
        maybeSource,
        source ->
            CodeableConcept.builder()
                .text(source.getText())
                .coding(codings(source.getCoding()))
                .build());
  }
}