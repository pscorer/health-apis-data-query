package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.Appointment;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.Encounter;
import gov.va.api.health.argonaut.api.resources.Immunization;
import gov.va.api.health.argonaut.api.resources.Location;
import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.MedicationDispense;
import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationStatement;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Organization;
import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.api.health.argonaut.api.resources.Practitioner;
import gov.va.api.health.argonaut.api.resources.Procedure;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@SuppressWarnings({"DefaultAnnotationParam", "WeakerAccess"})
@RunWith(Parameterized.class)
@Slf4j
public class ArgonautReadAndSearchIT {

  @Parameter(0)
  public int status;

  @Parameter(1)
  public Class<?> response;

  @Parameter(2)
  public String path;

  @Parameter(3)
  public String[] params;

  public static Object[] expect(int status, Class<?> response, String path, String... parameters) {
    return new Object[] {status, response, path, parameters};
  }

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        // Allergy Intolerance
        expect(
            200,
            AllergyIntolerance.class,
            "/api/AllergyIntolerance/{id}",
            ids.allergyIntolerance()),
        expect(404, OperationOutcome.class, "/api/AllergyIntolerance/{id}", ids.unknown()),
        expect(
            200,
            AllergyIntolerance.Bundle.class,
            "/api/AllergyIntolerance?_id={id}",
            ids.allergyIntolerance()),
        expect(
            200,
            AllergyIntolerance.Bundle.class,
            "/api/AllergyIntolerance?identifier={id}",
            ids.allergyIntolerance()),
        expect(
            200,
            AllergyIntolerance.Bundle.class,
            "/api/AllergyIntolerance?patient={patient}",
            ids.patient()),
        // Appointment
        expect(200, Appointment.class, "/api/Appointment/{id}", ids.appointment()),
        expect(404, OperationOutcome.class, "/api/Appointment/{id}", ids.unknown()),
        expect(200, Appointment.Bundle.class, "/api/Appointment?_id={id}", ids.appointment()),
        expect(
            200, Appointment.Bundle.class, "/api/Appointment?identifier={id}", ids.appointment()),
        expect(200, Appointment.Bundle.class, "/api/Appointment?patient={patient}", ids.patient()),
        // Condition
        expect(200, Condition.class, "/api/Condition/{id}", ids.condition()),
        expect(404, OperationOutcome.class, "/api/Condition/{id}", ids.unknown()),
        expect(200, Condition.Bundle.class, "/api/Condition?identifier={id}", ids.condition()),
        expect(404, OperationOutcome.class, "/api/Condition?_id={id}", ids.unknown()),
        expect(200, Condition.Bundle.class, "/api/Condition?patient={patient}", ids.patient()),
        expect(
            200,
            Condition.Bundle.class,
            "/api/Condition?patient={patient}&category=problem",
            ids.patient()),
        expect(
            200,
            Condition.Bundle.class,
            "/api/Condition?patient={patient}&category=health-concern",
            ids.patient()),
        expect(
            200,
            Condition.Bundle.class,
            "/api/Condition?patient={patient}&clinicalstatus=active",
            ids.patient()),
        // DiagnosticReport
        expect(200, DiagnosticReport.class, "/api/DiagnosticReport/{id}", ids.diagnosticReport()),
        expect(404, OperationOutcome.class, "/api/DiagnosticReport/{id}", ids.unknown()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "api/DiagnosticReport?_id={id}",
            ids.diagnosticReport()),
        expect(404, OperationOutcome.class, "api/DiagnosticReport?_id={id}", ids.unknown()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "api/DiagnosticReport?identifier={id}",
            ids.diagnosticReport()),
        expect(404, OperationOutcome.class, "/api/DiagnosticReport?identifier={id}", ids.unknown()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}",
            ids.patient()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB",
            ids.patient()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&code={loinc1}",
            ids.patient(),
            ids.diagnosticReports().loinc1()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&code={loinc1},{loinc2}",
            ids.patient(),
            ids.diagnosticReports().loinc1(),
            ids.diagnosticReports().loinc2()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={onDate}",
            ids.patient(),
            ids.diagnosticReports().onDate()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={fromDate}&date={toDate}",
            ids.patient(),
            ids.diagnosticReports().fromDate(),
            ids.diagnosticReports().toDate()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYear}",
            ids.patient(),
            ids.diagnosticReports().dateYear()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonth}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonth()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDay}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDay()),
        expect(
            400,
            OperationOutcome.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHour}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHour()),
        expect(
            400,
            OperationOutcome.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinute}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinute()),
        expect(
            400,
            OperationOutcome.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinuteSecond}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinuteSecond()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinuteSecondTimezone}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinuteSecondTimezone()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinuteSecondZulu}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinuteSecondZulu()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateGreaterThan}",
            ids.patient(),
            ids.diagnosticReports().dateGreaterThan()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateNotEqual}",
            ids.patient(),
            ids.diagnosticReports().dateNotEqual()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateStartsWith}",
            ids.patient(),
            ids.diagnosticReports().dateStartsWith()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateNoPrefix}",
            ids.patient(),
            ids.diagnosticReports().dateNoPrefix()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateEqual}",
            ids.patient(),
            ids.diagnosticReports().dateEqual()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateLessOrEqual}",
            ids.patient(),
            ids.diagnosticReports().dateLessOrEqual()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateLessThan}",
            ids.patient(),
            ids.diagnosticReports().dateLessThan()),
        // Encounter
        expect(200, Encounter.class, "/api/Encounter/{id}", ids.encounter()),
        expect(404, OperationOutcome.class, "/api/Encounter/{id}", ids.unknown()),
        expect(200, Encounter.Bundle.class, "/api/Encounter?_id={id}", ids.encounter()),
        expect(200, Encounter.Bundle.class, "/api/Encounter?identifier={id}", ids.encounter()),
        expect(404, OperationOutcome.class, "/api/Encounter?_id={id}", ids.unknown()),
        // Immunization
        expect(200, Immunization.class, "/api/Immunization/{id}", ids.immunization()),
        expect(404, OperationOutcome.class, "/api/Immunization/{id}", ids.unknown()),
        expect(200, Immunization.Bundle.class, "/api/Immunization?_id={id}", ids.immunization()),
        expect(
            200,
            Immunization.Bundle.class,
            "/api/Immunization?identifier={id}",
            ids.immunization()),
        expect(404, OperationOutcome.class, "/api/Immunization?_id={id}", ids.unknown()),
        expect(
            200, Immunization.Bundle.class, "/api/Immunization?patient={patient}", ids.patient()),
        // Location
        expect(200, Location.class, "/api/Location/{id}", ids.location()),
        expect(404, OperationOutcome.class, "/api/Location/{id}", ids.unknown()),
        expect(200, Location.Bundle.class, "/api/Location?_id={id}", ids.location()),
        expect(200, Location.Bundle.class, "/api/Location?identifier={id}", ids.location()),
        expect(404, OperationOutcome.class, "/api/Location?_id={id}", ids.unknown()),
        // Medication
        expect(200, Medication.class, "/api/Medication/{id}", ids.medication()),
        expect(404, OperationOutcome.class, "/api/Medication/{id}", ids.unknown()),
        expect(200, Medication.Bundle.class, "/api/Medication?_id={id}", ids.medication()),
        expect(200, Medication.Bundle.class, "/api/Medication?identifier={id}", ids.medication()),
        expect(404, OperationOutcome.class, "/api/Medication?_id={id}", ids.unknown()),
        // Medication Dispense
        expect(
            200,
            MedicationDispense.class,
            "/api/MedicationDispense/{id}",
            ids.medicationDispense()),
        expect(404, OperationOutcome.class, "/api/MedicationDispense/{id}", ids.unknown()),
        expect(
            200,
            MedicationDispense.Bundle.class,
            "/api/MedicationDispense?_id={id}",
            ids.medicationDispense()),
        expect(
            200,
            MedicationDispense.Bundle.class,
            "/api/MedicationDispense?identifier={id}",
            ids.medicationDispense()),
        expect(
            200,
            MedicationDispense.Bundle.class,
            "/api/MedicationDispense?patient={patient}",
            ids.patient()),
        expect(
            200,
            MedicationDispense.Bundle.class,
            "/api/MedicationDispense?patient={patient}&status=stopped,completed",
            ids.patient()),
        expect(
            200,
            MedicationDispense.Bundle.class,
            "/api/MedicationDispense?patient={patient}&type=FF,UD",
            ids.patient()),
        // Medication Order
        expect(200, MedicationOrder.class, "/api/MedicationOrder/{id}", ids.medicationOrder()),
        expect(404, OperationOutcome.class, "/api/MedicationOrder/{id}", ids.unknown()),
        expect(
            200,
            MedicationOrder.Bundle.class,
            "/api/MedicationOrder?_id={id}",
            ids.medicationOrder()),
        expect(
            200,
            MedicationOrder.Bundle.class,
            "/api/MedicationOrder?identifier={id}",
            ids.medicationOrder()),
        expect(
            200,
            MedicationOrder.Bundle.class,
            "/api/MedicationOrder?patient={patient}",
            ids.patient()),
        // MedicationStatement
        expect(
            200,
            MedicationStatement.class,
            "/api/MedicationStatement/{id}",
            ids.medicationStatement()),
        expect(404, OperationOutcome.class, "/api/MedicationStatement/{id}", ids.unknown()),
        expect(
            200,
            MedicationStatement.Bundle.class,
            "/api/MedicationStatement?_id={id}",
            ids.medicationStatement()),
        expect(
            200,
            MedicationStatement.Bundle.class,
            "/api/MedicationStatement?identifier={id}",
            ids.medicationStatement()),
        expect(404, OperationOutcome.class, "/api/MedicationStatement?_id={id}", ids.unknown()),
        expect(
            200,
            MedicationStatement.Bundle.class,
            "/api/MedicationStatement?patient={patient}",
            ids.patient()),
        // Observation
        expect(200, Observation.class, "/api/Observation/{id}", ids.observation()),
        expect(404, OperationOutcome.class, "/api/Observation/{id}", ids.unknown()),
        expect(200, Observation.Bundle.class, "/api/Observation?_id={id}", ids.observation()),
        expect(
            200, Observation.Bundle.class, "/api/Observation?identifier={id}", ids.observation()),
        expect(404, OperationOutcome.class, "/api/Observation?_id={id}", ids.unknown()),
        expect(200, Observation.Bundle.class, "/api/Observation?patient={patient}", ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory",
            ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory&date={date}",
            ids.patient(),
            ids.observations().onDate()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory&date={from}&date={to}",
            ids.patient(),
            ids.observations().dateRange().from(),
            ids.observations().dateRange().to()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=vital-signs",
            ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory,vital-signs",
            ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&code={loinc1}",
            ids.patient(),
            ids.observations().loinc1()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&code={loinc1},{loinc2}",
            ids.patient(),
            ids.observations().loinc1(),
            ids.observations().loinc2()),
        // Organization
        expect(200, Organization.class, "/api/Organization/{id}", ids.organization()),
        expect(404, OperationOutcome.class, "/api/Organization/{id}", ids.unknown()),
        expect(200, Organization.Bundle.class, "/api/Organization?_id={id}", ids.organization()),
        expect(
            200,
            Organization.Bundle.class,
            "/api/Organization?identifier={id}",
            ids.organization()),
        expect(404, OperationOutcome.class, "/api/Organization?_id={id}", ids.unknown()),
        // Patient
        expect(200, Patient.class, "/api/Patient/{id}", ids.patient()),
        expect(404, OperationOutcome.class, "/api/Patient/{id}", ids.unknown()),
        expect(200, Patient.Bundle.class, "/api/Patient?_id={id}", ids.patient()),
        expect(200, Patient.Bundle.class, "/api/Patient?identifier={id}", ids.patient()),
        expect(404, OperationOutcome.class, "/api/Patient?_id={id}", ids.unknown()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?family={family}&gender={gender}",
            ids.pii().family(),
            ids.pii().gender()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?given={given}&gender={gender}",
            ids.pii().given(),
            ids.pii().gender()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&birthdate={birthdate}",
            ids.pii().name(),
            ids.pii().birthdate()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&gender={gender}",
            ids.pii().name(),
            ids.pii().gender()),
        // Practitioner
        expect(200, Practitioner.class, "/api/Practitioner/{id}", ids.practitioner()),
        expect(404, OperationOutcome.class, "/api/Practitioner/{id}", ids.unknown()),
        expect(200, Practitioner.Bundle.class, "/api/Practitioner?_id={id}", ids.practitioner()),
        expect(
            200,
            Practitioner.Bundle.class,
            "/api/Practitioner?identifier={id}",
            ids.practitioner()),
        expect(404, OperationOutcome.class, "/api/Practitioner?_id={id}", ids.unknown()),
        // Procedure
        expect(200, Procedure.class, "/api/Procedure/{id}", ids.procedure()),
        expect(404, OperationOutcome.class, "/api/Procedure/{id}", ids.unknown()),
        expect(200, Procedure.Bundle.class, "/api/Procedure?_id={id}", ids.procedure()),
        expect(404, OperationOutcome.class, "/api/Procedure?_id={id}", ids.unknown()),
        expect(200, Procedure.Bundle.class, "/api/Procedure?identifier={id}", ids.procedure()),
        expect(200, Procedure.Bundle.class, "/api/Procedure?patient={patient}", ids.patient()),
        expect(
            200,
            Procedure.Bundle.class,
            "api/Procedure?patient={patient}&date={onDate}",
            ids.patient(),
            ids.procedures().onDate()),
        expect(
            200,
            Procedure.Bundle.class,
            "api/Procedure?patient={patient}&date={fromDate}&date={toDate}",
            ids.patient(),
            ids.procedures().fromDate(),
            ids.procedures().toDate()));
  }

  private TestClient argonaut() {
    return Sentinel.get().clients().argonaut();
  }

  @Test
  public void getResource() {
    argonaut().get(path, params).expect(status).expectValid(response);
  }

  /**
   * If the response is a bundle, then the query is a search. We want to verify paging parameters
   * restrict page >= 1, _count >=1, and _count <= 20
   */
  @Test
  public void pagingParameterBounds() {
    if (!AbstractBundle.class.isAssignableFrom(response)) {
      return;
    }
    argonaut().get(path + "&page=0", params).expect(400).expectValid(OperationOutcome.class);
    argonaut().get(path + "&_count=-1", params).expect(400).expectValid(OperationOutcome.class);
    argonaut().get(path + "&_count=0", params).expect(200).expectValid(response);
    argonaut().get(path + "&_count=21", params).expect(200).expectValid(response);
  }
}
