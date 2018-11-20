package gov.va.api.health.argonaut.service.controller.medication;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Narrative;
import gov.va.api.health.argonaut.api.elements.Narrative.NarrativeStatus;
import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.Medication.Product;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication.CdwProduct;
import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class MedicationTransformerTest {

  private final XmlSampleData cdw = new XmlSampleData();
  private final MedicationSampleData expectedMedication = new MedicationSampleData();

  @Test
  public void codeCodingListTransformsToCodingList() {
    List<Coding> testCodingList = transformer().code(cdw.code()).coding();
    List<Coding> expectedCodingList = expectedMedication.code().coding();
    assertThat(testCodingList).isEqualTo(expectedCodingList);
  }

  @Test
  public void codeReturnsNullForNull() {
    assertThat(transformer().code(null)).isNull();
  }

  @Test
  public void codeTransformsToCodeableConcept() {
    CodeableConcept testCdwCode = transformer().code(cdw.code());
    CodeableConcept expectedCdwCode = expectedMedication.medication().code();
    assertThat(testCdwCode).isEqualTo(expectedCdwCode);
  }

  @Test
  public void codingReturnsNullForEmptyList() {
    LinkedList<CdwCoding> emptyCodings = new LinkedList<>();
    assertThat(transformer().coding(emptyCodings)).isNull();
  }

  @Test
  public void codingReturnsNullForNull() {
    assertThat(transformer().coding(null)).isNull();
  }

  @Test
  public void medication101TransformsToModelMedication() {
    Medication test = transformer().apply(cdw.medication());
    Medication expected = expectedMedication.medication();
    assertThat(test).isEqualTo(expected);
  }

  @Test
  public void productFormCodingTransformsToCodingList() {
    List<Coding> testCoding =
        transformer().productForm(cdw.medication().getProduct().getForm()).coding();
    List<Coding> expectedCoding = expectedMedication.medication().product().form().coding();
    assertThat(testCoding).isEqualTo(expectedCoding);
  }

  @Test
  public void productFormTransformsToCodeableConcept() {
    CodeableConcept testCodeableConcept =
        transformer().productForm(cdw.medication().getProduct().getForm());
    CodeableConcept expectedCodeableConcept = expectedMedication.medication().product().form();
    assertThat(testCodeableConcept).isEqualTo(expectedCodeableConcept);
  }

  @Test
  public void productReturnsNullForNull() {
    assertThat(transformer().product(null)).isNull();
  }

  @Test
  public void textNarrativeIsNullIfTextIsNull() {
    assertThat(transformer().text(null)).isNull();
  }

  @Test
  public void textNarrativeTransformsString() {
    Narrative expected =
        Narrative.builder().div("<div>hello</div>").status(NarrativeStatus.additional).build();
    Narrative actual = transformer().text("hello");
    assertThat(actual).isEqualTo(expected);
  }

  private MedicationTransformer transformer() {
    return new MedicationTransformer();
  }

  private static class MedicationSampleData {

    CodeableConcept code() {
      return codeableConcept("code");
    }

    private CodeableConcept codeableConcept(String prefix) {
      return CodeableConcept.builder().coding(coding(prefix)).text(prefix + " text").build();
    }

    private List<Coding> coding(String prefix) {
      return Collections.singletonList(
          Coding.builder()
              .system(prefix + " system")
              .code(prefix + " code")
              .display(prefix + " display")
              .build());
    }

    CodeableConcept form() {
      return codeableConcept("form");
    }

    Medication medication() {
      return Medication.builder()
          .resourceType("Medication")
          .id("123456789")
          .text(text())
          .code(code())
          .product(product())
          .build();
    }

    Product product() {
      return Product.builder().id("1234").form(form()).build();
    }

    Narrative text() {
      return Narrative.builder().div("<div>hello</div>").status(NarrativeStatus.additional).build();
    }
  }

  private static class XmlSampleData {

    CdwCodeableConcept code() {
      return codeableConcept("code");
    }

    CdwCodeableConcept codeableConcept(String prefix) {
      CdwCodeableConcept code = new CdwCodeableConcept();
      code.getCoding().add(coding(prefix));
      code.setText(prefix + " text");
      return code;
    }

    CdwCoding coding(String prefix) {
      CdwCoding coding = new CdwCoding();
      coding.setSystem(prefix + " system");
      coding.setCode(prefix + " code");
      coding.setDisplay(prefix + " display");
      return coding;
    }

    CdwCodeableConcept form() {
      return codeableConcept("form");
    }

    CdwMedication medication() {
      CdwMedication medication = new CdwMedication();
      medication.setCdwId("123456789");
      medication.setRowNumber(new BigInteger("1"));
      medication.setText("hello");
      medication.setCode(code());
      medication.setProduct(product());
      return medication;
    }

    CdwProduct product() {
      CdwProduct product = new CdwProduct();
      product.setId("1234");
      product.setForm(form());
      return product;
    }
  }
}