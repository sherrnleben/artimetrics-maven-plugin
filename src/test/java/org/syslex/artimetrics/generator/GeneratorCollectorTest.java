package org.syslex.artimetrics.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.syslex.artimetrics.report.Report;

import java.time.OffsetDateTime;

public class GeneratorCollectorTest {

    @Test
    @DisplayName("test collect with existing pom.xml")
    public void collectGenerator() {
        final var report = new Report();
        new GeneratorCollector().supply(report);
        Assertions.assertNotNull(report.generator);
        Assertions.assertEquals("artimetrics-maven-plugin", report.generator.name);
        Assertions.assertNotNull(report.generator.version);
        Assertions.assertTrue(report.generator.version.length() > 4);
        Assertions.assertTrue(report.generator.generatedAt.isBefore(OffsetDateTime.now().plusSeconds(1)));
        Assertions.assertTrue(report.generator.generatedAt.isAfter(OffsetDateTime.now().minusSeconds(5)));
    }

}