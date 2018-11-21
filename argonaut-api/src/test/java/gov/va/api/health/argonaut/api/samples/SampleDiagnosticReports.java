package gov.va.api.health.argonaut.api.samples;

import static java.util.Collections.singletonList;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import java.util.Arrays;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(staticName = "get")
public class SampleDiagnosticReports {

  @Delegate SampleDataTypes dataTypes = SampleDataTypes.get();

  private CodeableConcept category() {
    return CodeableConcept.builder()
        .coding(
            singletonList(
                Coding.builder()
                    .system("http://hl7.org/fhir/ValueSet/diagnostic-service-sections")
                    .code("ok")
                    .build()))
        .text("dat category")
        .build();
  }

  public DiagnosticReport diagnosticReport() {
    return DiagnosticReport.builder()
        .id("1234")
        .resourceType("Diagnostic Report")
        .meta(meta())
        .implicitRules("https://HelloRules.com")
        .language("Hello Language")
        .text(narrative())
        .contained(singletonList(resource()))
        .extension(Arrays.asList(extension(), extension()))
        .modifierExtension(
            Arrays.asList(extension(), extensionWithQuantity(), extensionWithRatio()))
        .identifier(singletonList(identifier()))
        .status(DiagnosticReport.Code._final)
        .category(category())
        .code(codeableConcept())
        .subject(reference())
        .encounter(reference())
        .effectiveDateTime("2000-10-01")
        .issued("1970-01-01T00:00:00Z")
        .performer(reference())
        .request(Arrays.asList(reference(), reference()))
        .specimen(Arrays.asList(reference(), reference(), reference()))
        .result(singletonList(reference()))
        .imagingStudy(singletonList(reference()))
        .image(image())
        .conclusion("The end.")
        .codedDiagnosis(Arrays.asList(codeableConcept(), codeableConcept()))
        .presentedForm(Arrays.asList(attachment(), attachment()))
        .build();
  }

  private List<DiagnosticReport.Image> image() {
    return singletonList(
        DiagnosticReport.Image.builder()
            .id("456")
            .extension(Arrays.asList(extension(), extension()))
            .modifierExtension(
                Arrays.asList(extension(), extensionWithQuantity(), extensionWithRatio()))
            .comment("Hello?")
            .link(reference())
            .build());
  }
}
