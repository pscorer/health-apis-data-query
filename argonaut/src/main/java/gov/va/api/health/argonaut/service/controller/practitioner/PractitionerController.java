package gov.va.api.health.argonaut.service.controller.practitioner;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Practitioner;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Bundler.BundleContext;
import gov.va.api.health.argonaut.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.controller.Validator;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.api.health.argonaut.service.mranderson.client.Query.Profile;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root;
import java.util.Collections;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Request Mappings for the Argonaut Practitioner Profile, see
 * http://hl7.org/fhir/DSTU2/practitioner.html for implementation details.
 */
@SuppressWarnings("WeakerAccess")
@RestController
@RequestMapping(
  value = {"/api/Practitioner"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class PractitionerController {

  private Transformer transformer;
  private MrAndersonClient mrAndersonClient;
  private Bundler bundler;

  private Practitioner.Bundle bundle(
      MultiValueMap<String, String> parameters, int page, int count) {

    CdwPractitioner100Root root = search(parameters);
    LinkConfig linkConfig =
        LinkConfig.builder()
            .path("Practitioner")
            .queryParams(parameters)
            .page(page)
            .recordsPerPage(count)
            .totalRecords(root.getRecordCount().intValue())
            .build();
    return bundler.bundle(
        BundleContext.of(
            linkConfig,
            root.getPractitioners() == null
                ? Collections.emptyList()
                : root.getPractitioners().getPractitioner(),
            transformer,
            Practitioner.Entry::new,
            Practitioner.Bundle::new));
  }

  /** Read by id. */
  @GetMapping(value = {"/{publicId}"})
  public Practitioner read(@PathVariable("publicId") String publicId) {

    return transformer.apply(
        firstPayloadItem(
            hasPayload(
                search(Parameters.forIdentity(publicId)).getPractitioners().getPractitioner())));
  }

  private CdwPractitioner100Root search(MultiValueMap<String, String> params) {
    Query<CdwPractitioner100Root> query =
        Query.forType(CdwPractitioner100Root.class)
            .profile(Profile.DSTU2)
            .resource("Practitioner")
            .version("1.00")
            .parameters(params)
            .build();
    return mrAndersonClient.search(query);
  }

  /** Search by _id. */
  @GetMapping(params = {"_id"})
  public Practitioner.Bundle searchById(
      @RequestParam("_id") String id,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "_count", defaultValue = "1") int count) {
    return bundle(
        Parameters.builder().add("identifier", id).add("page", page).add("_count", count).build(),
        page,
        count);
  }

  /** Search by Identifier. */
  @GetMapping(params = {"identifier"})
  public Practitioner.Bundle searchByIdentifier(
      @RequestParam("identifier") String id,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "_count", defaultValue = "1") int count) {
    return bundle(
        Parameters.builder().add("identifier", id).add("page", page).add("_count", count).build(),
        page,
        count);
  }

  /** Hey, this is a validate endpoint. It validates. */
  @PostMapping(
    value = "/$validate",
    consumes = {"application/json", "application/json+fhir", "application/fhir+json"}
  )
  public OperationOutcome validate(@RequestBody Practitioner.Bundle bundle) {
    return Validator.create().validate(bundle);
  }

  public interface Transformer
      extends Function<CdwPractitioner100Root.CdwPractitioners.CdwPractitioner, Practitioner> {}
}
