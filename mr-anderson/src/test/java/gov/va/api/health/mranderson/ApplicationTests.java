package gov.va.api.health.mranderson;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
  properties = {
    "security.require-ssl=true",
    "server.ssl.key-store-type=JKS",
    "server.ssl.key-store=classpath:test-keystore.jks",
    "server.ssl.key-store-password=secret",
    "server.ssl.key-alias=test",
    "ssl.key-store=classpath:test-keystore.jks",
    "ssl.key-store-password=secret",
    "ssl.client-key-password=secret",
    "ssl.use-trust-store=true",
    "ssl.trust-store=classpath:test-truststore.jks",
    "ssl.trust-store-password=secret"
  }
)
public class ApplicationTests {

  @Test
  public void contextLoads() {
    /* Verifies that the application starts. */
    assertTrue(true);
  }
}
