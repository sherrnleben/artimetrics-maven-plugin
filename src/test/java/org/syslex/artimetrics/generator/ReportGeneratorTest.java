package org.syslex.artimetrics.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.syslex.artimetrics.report.Report;

import java.time.OffsetDateTime;

public class ReportGeneratorTest {

    @Test
    @DisplayName("collect generator")
    public void collectGenerator() {
        final var report = new Report();
        ReportGenerator.init(null).collectGenerator(report);
        Assertions.assertNotNull(report.generator);
        Assertions.assertEquals("artimetrics-maven-plugin", report.generator.name);
        Assertions.assertNotNull(report.generator.version);
        Assertions.assertTrue(report.generator.version.length() > 4);
        Assertions.assertTrue(report.generator.generatedAt.isBefore(OffsetDateTime.now().plusSeconds(1)));
        Assertions.assertTrue(report.generator.generatedAt.isAfter(OffsetDateTime.now().minusSeconds(5)));
    }

}