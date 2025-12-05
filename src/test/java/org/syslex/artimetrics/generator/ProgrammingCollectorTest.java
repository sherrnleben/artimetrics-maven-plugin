package org.syslex.artimetrics.generator;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.syslex.artimetrics.report.Report;

import java.util.Properties;

public class ProgrammingCollectorTest {

    static Model buildExampleModelWithJavaVersions() {
        final var properties = new Properties();
        properties.setProperty("maven.compiler.release", "1.1");
        properties.setProperty("maven.compiler.target", "1.2");
        properties.setProperty("maven.compiler.source", "1.3");

        //noinspection ExtractMethodRecommender
        final var release = new Xpp3Dom("release");
        release.setValue("2.1");
        final var target = new Xpp3Dom("target");
        target.setValue("2.2");
        final var source = new Xpp3Dom("source");
        source.setValue("2.3");

        final var pluginConfig = new Xpp3Dom("<configuration>");
        pluginConfig.addChild(release);
        pluginConfig.addChild(target);
        pluginConfig.addChild(source);

        final var plugin = new Plugin();
        plugin.setArtifactId("maven-compiler-plugin");
        plugin.setConfiguration(pluginConfig);

        final var model = new Model();
        model.setProperties(properties);
        model.setBuild(new Build());
        model.getBuild().addPlugin(plugin);

        return model;
    }

    @Test
    @DisplayName("collect programming: release version from properties")
    public void collectProgramming_propertiesRelease() {
        final var model = buildExampleModelWithJavaVersions();
        final var report = new Report();
        new ProgrammingCollector(model).supply(report);
        Assertions.assertEquals("1.1", report.language.version);
    }

    @Test
    @DisplayName("collect programming: target version from properties")
    public void collectProgramming_propertiesTarget() {
        final var model = buildExampleModelWithJavaVersions();
        model.getProperties().remove("maven.compiler.release");
        final var report = new Report();
        new ProgrammingCollector(model).supply(report);
        Assertions.assertEquals("1.2", report.language.version);
    }

    @Test
    @DisplayName("collect programming: source version from properties")
    public void collectProgramming_propertiesSource() {
        final var model = buildExampleModelWithJavaVersions();
        model.getProperties().remove("maven.compiler.release");
        model.getProperties().remove("maven.compiler.target");
        final var report = new Report();
        new ProgrammingCollector(model).supply(report);
        Assertions.assertEquals("1.3", report.language.version);
    }

    @Test
    @DisplayName("collect programming: source version from maven compiler plugin")
    public void collectProgramming_pluginRelease() {
        final var model = buildExampleModelWithJavaVersions();
        model.setProperties(null);
        final var report = new Report();
        new ProgrammingCollector(model).supply(report);
        Assertions.assertEquals("2.1", report.language.version);
    }

    @Test
    @DisplayName("collect programming: target version from maven compiler plugin")
    public void collectProgramming_pluginTarget() {
        final var model = buildExampleModelWithJavaVersions();
        model.setProperties(null);
        ((Xpp3Dom) (model.getBuild().getPlugins().getFirst()).getConfiguration()).removeChild(0);
        final var report = new Report();
        new ProgrammingCollector(model).supply(report);
        Assertions.assertEquals("2.2", report.language.version);
    }

    @Test
    @DisplayName("collect programming: source version from maven compiler plugin")
    public void collectProgramming_pluginSource() {
        final var model = buildExampleModelWithJavaVersions();
        model.setProperties(null);
        ((Xpp3Dom) (model.getBuild().getPlugins().getFirst()).getConfiguration()).removeChild(0);
        ((Xpp3Dom) (model.getBuild().getPlugins().getFirst()).getConfiguration()).removeChild(0);
        final var report = new Report();
        new ProgrammingCollector(model).supply(report);
        Assertions.assertEquals("2.3", report.language.version);
    }

    @Test
    @DisplayName("collect programming: no version found")
    public void collectProgramming_none() {
        final var model = buildExampleModelWithJavaVersions();
        model.setProperties(null);
        model.setBuild(null);
        final var report = new Report();
        new ProgrammingCollector(model).supply(report);
        Assertions.assertNull(report.language.version);
    }

}