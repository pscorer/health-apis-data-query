package gov.va.api.health.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.sentinel.categories.Manual;
import gov.va.api.health.sentinel.selenium.IdMeOauthRobot;
import gov.va.api.health.sentinel.selenium.IdMeOauthRobot.Configuration.UserCredentials;
import gov.va.api.health.sentinel.selenium.IdMeOauthRobot.TokenExchange;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Slf4j
public class LabLogInTest {
  @Test
  @Category(Manual.class)
  @SneakyThrows
  public void login() {
    List<String> ids = new LinkedList<>();
    for (int i = 1; i <= 5; i++) {
      ids.add("vasdvp+IDME_" + String.format("%02d", i) + "@gmail.com");
    }

    for (int i = 101; i <= 183; i++) {
      ids.add("va.api.user+idme." + String.format("%03d", i) + "@gmail.com");
    }

    List<String> winners = new CopyOnWriteArrayList<>();
    List<String> losers = new CopyOnWriteArrayList<>();
    LabRobots robots = LabRobots.fromSystemProperties();

    ExecutorService ex = Executors.newFixedThreadPool(10);

    List<Future<?>> futures = new ArrayList<>(ids.size());

    for (String id : ids) {
      futures.add(
          ex.submit(
              () -> {
                log.info("Checking {}", id);
                IdMeOauthRobot robot =
                    robots.makeRobot(
                        UserCredentials.builder()
                            .id(id)
                            .password(robots.labConfig().userPassword())
                            .build());
                try {
                  TokenExchange token = robot.token();
                  if (token.isError()) {
                    log.error(
                        "LOSER: {} is a loser: {}, {}",
                        id,
                        token.error(),
                        token.errorDescription());
                    losers.add(id + " - " + token.error() + ": " + token.errorDescription());
                  } else {
                    log.info("WINNER: {} is patient {}.", id, token.patient());
                    winners.add(id);
                  }
                } catch (Exception e) {
                  log.error(
                      "LOSER: {} ({}/{})", e.getMessage(), id, robots.labConfig().userPassword());
                  losers.add(id + " - " + e.getMessage());
                }
              }));
    }

    ex.shutdown();
    ex.awaitTermination(10, TimeUnit.MINUTES);
    futures.forEach(
        f -> {
          try {
            f.get();
          } catch (Exception e) {
            log.error(e.getMessage());
          }
        });

    String report =
        Stream.concat(winners.stream().map(w -> w + " - OK"), losers.stream())
            .sorted()
            .collect(Collectors.joining("\n"));
    Files.write(new File("lab-users.txt").toPath(), report.getBytes(StandardCharsets.UTF_8));
    log.info("Lab Users:\n{}", report);
    assertThat(losers.size()).isZero();
  }
}
