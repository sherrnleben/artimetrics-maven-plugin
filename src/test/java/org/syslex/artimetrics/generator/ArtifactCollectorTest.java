package org.syslex.artimetrics.generator;

import org.apache.maven.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.syslex.artimetrics.report.Report;

public class ArtifactCollectorTest {

    @Test
    @DisplayName("test collect with no identify values set")
    public void testCollect_identityNull() {
        final var model = new Model();
        final var report = new Report();
        new ArtifactCollector(model).supply(report);
        Assertions.assertNotNull(report.artifact);
        Assertions.assertNull(report.artifact.group);
        Assertions.assertNull(report.artifact.name);
        Assertions.assertNull(report.artifact.version);
    }

    @Test
    @DisplayName("test collect by reading group ID")
    public void testCollect_readGroupId() {
        final var model = new Model();
        model.setGroupId("com.example");
        final var report = new Report();
        new ArtifactCollector(model).supply(report);
        Assertions.assertEquals("com.example", report.artifact.group);
    }

    @Test
    @DisplayName("test collect by reading group ID")
    public void testCollect_readArtifactId() {
        final var model = new Model();
        model.setArtifactId("my-application");
        final var report = new Report();
        new ArtifactCollector(model).supply(report);
        Assertions.assertEquals("my-application", report.artifact.name);
    }

    @Test
    @DisplayName("test collect by reading artifact version")
    public void testCollect_readVersion() {
        final var model = new Model();
        model.setVersion("1.0.5-SNAPSHOT");
        final var report = new Report();
        new ArtifactCollector(model).supply(report);
        Assertions.assertEquals("1.0.5-SNAPSHOT", report.artifact.version);
    }

}