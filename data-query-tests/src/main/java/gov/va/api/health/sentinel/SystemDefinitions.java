package gov.va.api.health.sentinel;

import static gov.va.api.health.sentinel.SentinelProperties.magicAccessToken;

import gov.va.api.health.sentinel.TestIds.DiagnosticReports;
import gov.va.api.health.sentinel.TestIds.Observations;
import gov.va.api.health.sentinel.TestIds.PersonallyIdentifiableInformation;
import gov.va.api.health.sentinel.TestIds.Procedures;
import gov.va.api.health.sentinel.TestIds.Range;
import java.util.Optional;
import lombok.experimental.UtilityClass;

/**
 * {@link SystemDefinition}s for different environments. {@link #systemDefinition()} method provides
 * the appropriate implementation for the current environment.
 */
@UtilityClass
public final class SystemDefinitions {
  private static DiagnosticReports diagnosticReports() {
    return DiagnosticReports.builder()
        .loinc1("10000-8")
        .loinc2("10001-6")
        .badLoinc("99999-9")
        .onDate("eq1970-01-01")
        .fromDate("gt1970-01-01")
        .toDate("lt2038-01-01")
        .dateYear("ge1970")
        .dateYearMonth("ge1970-01")
        .dateYearMonthDay("ge1970-01-01")
        .dateYearMonthDayHour("ge1970-01-01T07")
        .dateYearMonthDayHourMinute("ge1970-01-01T07:00")
        .dateYearMonthDayHourMinuteSecond("ge1970-01-01T07:00:00")
        .dateYearMonthDayHourMinuteSecondTimezone("ge1970-01-01T07:00:00+05:00")
        .dateYearMonthDayHourMinuteSecondZulu("ge1970-01-01T07:00:00Z")
        .dateGreaterThan("ge1970-01-01")
        .dateNotEqual("ne1970-01-01")
        .dateStartsWith("sa1970-01-01")
        .dateNoPrefix("1970-01-01")
        .dateEqual("1970-01-01")
        .dateLessOrEqual("le2038-01-19")
        .dateLessThan("lt2038-01-19")
        .build();
  }

  /** Return definitions for the lab environment. */
  private static SystemDefinition lab() {
    String url = "https://dev-api.va.gov";
    return SystemDefinition.builder()
        .ids(serviceDefinition("ids", url, 443, null, "/not-available/"))
        .mrAnderson(serviceDefinition("mr-anderson", url, 443, null, "/not-available/"))
        .dataQuery(
            serviceDefinition("argonaut", url, 443, magicAccessToken(), "/services/argonaut/v0/"))
        .cdwIds(labAndStagingIds())
        .build();
  }

  private static TestIds labAndStagingIds() {
    return TestIds.builder()
        .publicIds(true)
        .allergyIntolerance("17a7e128-8cf2-521f-ba99-b5eadb6ca598")
        .condition("000f2b73-ebf9-5d10-b45e-90813cd0e42e")
        .diagnosticReport("1303138b-1548-5663-963e-f346834681ab")
        .diagnosticReports(diagnosticReports())
        .immunization("1ec5f832-6faa-5146-925a-e2941a7a332c")
        .medication("2cb64dd0-ea52-503d-b61e-6060e84ff0ee")
        .medicationOrder("5a1428a7-fc73-5714-bc9c-670be3834164")
        .medicationStatement("6e484ab1-e7df-5c0b-8947-65a0bd03504c")
        .observation("1a21eae3-e08c-5f04-b7a7-fb6681fa2623")
        .observations(observations())
        .patient("1011537977V693883")
        .procedure("0cbfb880-048f-5cf4-b44f-fed3f7664c7b")
        .procedures(procedures())
        .location("unused")
        .appointment("unused")
        .medicationDispense("unused")
        .encounter("unused")
        .organization("unused")
        .practitioner("unused")
        .unknown("5555555555555")
        .build();
  }

  /**
   * Return system definitions for local running applications as started by the Maven build process.
   */
  private static SystemDefinition local() {
    String url = "https://localhost";
    return SystemDefinition.builder()
        .ids(serviceDefinition("ids", url, 8089, null, "/api/"))
        .mrAnderson(serviceDefinition("mr-anderson", url, 8088, null, "/api/"))
        .dataQuery(serviceDefinition("argonaut", url, 8090, null, "/api/"))
        .cdwIds(localIds())
        .build();
  }

  private static TestIds localIds() {
    return TestIds.builder()
        .publicIds(false)
        .allergyIntolerance("1000001782544")
        .appointment("1200438317388")
        .condition("1400007575530:P")
        .diagnosticReport("1000000031384:L")
        .encounter("1200753214085")
        .diagnosticReports(diagnosticReports())
        .immunization("1000000043979")
        .location("166365:L")
        .medication("212846")
        .medicationDispense("1200738474343:R")
        .medicationOrder("1200389904206:O")
        .medicationStatement("1400000182116")
        .observation("1201051417263:V")
        .observations(observations())
        .organization("1000025431:C")
        .pii(
            PersonallyIdentifiableInformation.builder()
                .gender("male")
                .birthdate("1970-01-01")
                .given("JOHN Q")
                .name("VETERAN,JOHN")
                .family("VETERAN")
                .build())
        .patient("185601V825290")
        .practitioner("10092125")
        .procedure("1400000140034")
        .procedures(procedures())
        .unknown("5555555555555")
        .build();
  }

  private static Observations observations() {
    return Observations.builder()
        .loinc1("72166-2")
        .loinc2("777-3")
        .badLoinc("99999-9")
        .onDate("2015-04-15")
        .dateRange(Range.allTime())
        .build();
  }

  private static Procedures procedures() {
    return Procedures.builder().fromDate("ge2009").onDate("ge2009").toDate("le2010").build();
  }

  /** Return definitions for the production environment. */
  private static SystemDefinition prod() {
    // Mr Anderson not accessible in this environment
    String url = "https://argonaut.lighthouse.va.gov";
    return SystemDefinition.builder()
        .ids(serviceDefinition("ids", url, 443, null, "/not-available/"))
        .mrAnderson(serviceDefinition("mr-anderson", url, 443, null, "/not-available/"))
        .dataQuery(serviceDefinition("argonaut", url, 443, magicAccessToken(), "/api/"))
        .cdwIds(prodAndQaIds())
        .build();
  }

  private static TestIds prodAndQaIds() {
    return TestIds.builder()
        .publicIds(true)
        .allergyIntolerance("3be00408-b0ff-598d-8ba1-1e0bbfb02b99")
        .appointment("f7721341-03ad-56cf-b0e5-e96fded23a1b")
        .condition("ea59bc29-d507-571b-a4c6-9ac0d2146c45")
        .diagnosticReport("0bca2c42-8d23-5d36-90b8-81a8b12bb1b5")
        .diagnosticReports(diagnosticReports())
        .encounter("05d66afc-3a1a-5277-8b26-a8084ac46a08")
        .immunization("00f4000a-b1c9-5190-993a-644569d2722b")
        .location("a146313b-9a77-5337-a442-bee6ceb4aa5c")
        .medication("89a46bce-8b95-5a91-bbef-1fb5f8a2a292")
        .medicationDispense("773bb1ab-4430-5012-b203-a88c41c5dde9")
        .medicationOrder("91f4a9d2-e7fa-5b34-a875-6d75761221c7")
        .medicationStatement("e4573ebc-40e4-51bb-9da1-20a91b31ff24")
        .observation("40e2ced6-32e2-503e-85b8-198690f6611b")
        .observations(observations())
        .organization("3e5dbe7a-72ca-5441-9287-0b639ae7a1bc")
        .patient("1011537977V693883")
        .practitioner("7b4c6b83-2c5a-5cbf-836c-875253fb9bf9")
        .procedure("c416df15-fc1d-5a04-ab11-34d7bf453d15")
        .procedures(procedures())
        .unknown("5555555555555")
        .build();
  }

  /** Return definitions for the qa environment. */
  private static SystemDefinition qa() {
    // ID service and Mr Anderson not accessible in this environment
    String url = "https://qa-argonaut.lighthouse.va.gov";
    return SystemDefinition.builder()
        .ids(serviceDefinition("ids", url, 443, null, "/not-available/"))
        .mrAnderson(serviceDefinition("mr-anderson", url, 443, null, "/not-available/"))
        .dataQuery(serviceDefinition("argonaut", url, 443, magicAccessToken(), "/api/"))
        .cdwIds(prodAndQaIds())
        .build();
  }

  private static ServiceDefinition serviceDefinition(
      String name, String url, int port, String accessToken, String apiPath) {
    return ServiceDefinition.builder()
        .url(SentinelProperties.optionUrl(name, url))
        .port(port)
        .accessToken(() -> Optional.ofNullable(accessToken))
        .apiPath(SentinelProperties.optionApiPath(name, apiPath))
        .build();
  }

  /** Return definitions for the staging environment. */
  private static SystemDefinition staging() {
    // ID service and Mr Anderson not accessible in this environment
    String url = "https://staging-argonaut.lighthouse.va.gov";
    return SystemDefinition.builder()
        .ids(serviceDefinition("ids", url, 443, null, "/not-available/"))
        .mrAnderson(serviceDefinition("mr-anderson", url, 443, null, "/not-available/"))
        .dataQuery(serviceDefinition("argonaut", url, 443, magicAccessToken(), "/api/"))
        .build();
  }

  /** Return the applicable system definition for the current environment. */
  public static SystemDefinition systemDefinition() {
    switch (Environment.get()) {
      case LAB:
        return lab();
      case LOCAL:
        return local();
      case PROD:
        return prod();
      case QA:
        return qa();
      case STAGING:
        return staging();
      default:
        throw new IllegalArgumentException("Unknown sentinel environment: " + Environment.get());
    }
  }
}
