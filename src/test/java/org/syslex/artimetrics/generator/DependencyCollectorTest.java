package org.syslex.artimetrics.generator;

import org.apache.maven.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.syslex.artimetrics.report.DependencyIntegration;
import org.syslex.artimetrics.report.Report;

import java.util.ArrayList;
import java.util.List;

public class DependencyCollectorTest {

    @Test
    @DisplayName("supply: minimalistic")
    public void supply_min() {
        final var pom = new Model();
        pom.setParent(null);
        pom.setBuild(null);
        pom.setDependencies(null);

        final var report = new Report();

        final var collector = new DependencyCollector(pom);
        collector.collectParent(report);

        Assertions.assertNull(report.dependencies);
    }

    @Test
    @DisplayName("supply: with parent")
    public void supply_parent() {
        final var parent = new Parent();
        parent.setGroupId("com.example");
        parent.setArtifactId("parent-artifact");
        parent.setVersion("1.8.0");

        final var pom = new Model();
        pom.setParent(parent);
        pom.setBuild(null);
        pom.setDependencies(null);

        final var report = new Report();

        final var collector = new DependencyCollector(pom);
        collector.supply(report);

        Assertions.assertEquals(1, report.dependencies.size());
        Assertions.assertEquals("com.example", report.dependencies.getFirst().group);
        Assertions.assertEquals("parent-artifact", report.dependencies.getFirst().name);
        Assertions.assertEquals("1.8.0", report.dependencies.getFirst().version);
        Assertions.assertEquals(DependencyIntegration.PARENT, report.dependencies.getFirst().integration);
    }

    @Test
    @DisplayName("supply: with dependency")
    public void supply_dependency() {
        final var dependency = new Dependency();
        dependency.setGroupId("com.example");
        dependency.setArtifactId("dependency-artifact");
        dependency.setVersion("1.14.0");

        final var pom = new Model();
        pom.setParent(null);
        pom.setBuild(null);
        pom.setDependencies(List.of(dependency));

        final var report = new Report();

        final var collector = new DependencyCollector(pom);
        collector.supply(report);

        Assertions.assertEquals(1, report.dependencies.size());
        Assertions.assertEquals("com.example", report.dependencies.getFirst().group);
        Assertions.assertEquals("dependency-artifact", report.dependencies.getFirst().name);
        Assertions.assertEquals("1.14.0", report.dependencies.getFirst().version);
        Assertions.assertEquals(DependencyIntegration.DEPENDENCY, report.dependencies.getFirst().integration);
    }

    @Test
    @DisplayName("supply: with plugin")
    public void supply_plugin() {
        final var plugin = new Plugin();
        plugin.setGroupId("com.example");
        plugin.setArtifactId("plugin-e");
        plugin.setVersion("2.33.0");

        final var pom = new Model();
        pom.setParent(null);
        pom.setBuild(new Build());
        pom.getBuild().setPlugins(List.of(plugin));
        pom.setDependencies(null);

        final var report = new Report();

        final var collector = new DependencyCollector(pom);
        collector.supply(report);

        Assertions.assertEquals(1, report.dependencies.size());
        Assertions.assertEquals("com.example", report.dependencies.getFirst().group);
        Assertions.assertEquals("plugin-e", report.dependencies.getFirst().name);
        Assertions.assertEquals("2.33.0", report.dependencies.getFirst().version);
        Assertions.assertEquals(DependencyIntegration.PLUGIN, report.dependencies.getFirst().integration);
    }

    @Test
    @DisplayName("collect parent: no parent defined")
    public void collectParent_none() {
        final var pom = new Model();
        pom.setParent(null);

        final var collector = new DependencyCollector(pom);
        final var report = new Report();
        collector.collectParent(report);

        Assertions.assertNull(report.dependencies);
    }

    @Test
    @DisplayName("collect parent: parent defined")
    public void collectParent_value() {
        final var pom = new Model();
        pom.setParent(new Parent());
        pom.getParent().setGroupId("com.example");
        pom.getParent().setArtifactId("parent-application");
        pom.getParent().setVersion("1.2.0");

        final var report = new Report();
        report.dependencies = new ArrayList<>();

        final var collector = new DependencyCollector(pom);
        collector.collectParent(report);

        Assertions.assertEquals(1, report.dependencies.size());
        Assertions.assertEquals("com.example", report.dependencies.getFirst().group);
        Assertions.assertEquals("parent-application", report.dependencies.getFirst().name);
        Assertions.assertEquals("1.2.0", report.dependencies.getFirst().version);
        Assertions.assertEquals(DependencyIntegration.PARENT, report.dependencies.getFirst().integration);
    }

    @Test
    @DisplayName("collect plugins: no plugins defined")
    public void collectPlugins_none() {
        final var pom = new Model();
        pom.setBuild(new Build());
        pom.getBuild().setPlugins(null);

        final var collector = new DependencyCollector(pom);
        final var report = new Report();
        collector.collectPlugins(report);

        Assertions.assertNull(report.dependencies);
    }

    @Test
    @DisplayName("collect plugins: two plugins defined")
    public void collectPlugins_values() {
        final var plugin1 = new Plugin();
        plugin1.setGroupId("com.example");
        plugin1.setArtifactId("plugin-a");
        plugin1.setVersion("1.0.4");

        final var plugin2 = new Plugin();
        plugin2.setGroupId("com.example");
        plugin2.setArtifactId("plugin-b");
        plugin2.setVersion("1.3.1");

        final var pom = new Model();
        pom.setBuild(new Build());
        pom.getBuild().setPlugins(List.of(plugin1, plugin2));

        final var report = new Report();
        report.dependencies = new ArrayList<>();

        final var collector = new DependencyCollector(pom);
        collector.collectPlugins(report);

        Assertions.assertEquals(2, report.dependencies.size());

        Assertions.assertEquals("com.example", report.dependencies.getFirst().group);
        Assertions.assertEquals("plugin-a", report.dependencies.getFirst().name);
        Assertions.assertEquals("1.0.4", report.dependencies.getFirst().version);
        Assertions.assertEquals(DependencyIntegration.PLUGIN, report.dependencies.getFirst().integration);

        Assertions.assertEquals("com.example", report.dependencies.get(1).group);
        Assertions.assertEquals("plugin-b", report.dependencies.get(1).name);
        Assertions.assertEquals("1.3.1", report.dependencies.get(1).version);
        Assertions.assertEquals(DependencyIntegration.PLUGIN, report.dependencies.get(1).integration);
    }

    @Test
    @DisplayName("collect dependencies: no dependencies defined")
    public void collectDependencies_none() {
        final var pom = new Model();
        pom.setDependencies(null);

        final var collector = new DependencyCollector(pom);
        final var report = new Report();
        collector.collectDependencies(report);

        Assertions.assertNull(report.dependencies);
    }

    @Test
    @DisplayName("collect dependencies: two dependencies defined")
    public void collectDependencies_values() {
        final var dependency1 = new Dependency();
        dependency1.setGroupId("com.example");
        dependency1.setArtifactId("dependency-a");
        dependency1.setVersion("2.0.4");

        final var dependency2 = new Dependency();
        dependency2.setGroupId("com.example");
        dependency2.setArtifactId("dependency-b");
        dependency2.setVersion("4.7.9-alpha");

        final var pom = new Model();
        pom.setDependencies(List.of(dependency1, dependency2));

        final var report = new Report();
        report.dependencies = new ArrayList<>();

        final var collector = new DependencyCollector(pom);
        collector.collectDependencies(report);

        Assertions.assertEquals(2, report.dependencies.size());

        Assertions.assertEquals("com.example", report.dependencies.getFirst().group);
        Assertions.assertEquals("dependency-a", report.dependencies.getFirst().name);
        Assertions.assertEquals("2.0.4", report.dependencies.getFirst().version);
        Assertions.assertEquals(DependencyIntegration.DEPENDENCY, report.dependencies.getFirst().integration);

        Assertions.assertEquals("com.example", report.dependencies.get(1).group);
        Assertions.assertEquals("dependency-b", report.dependencies.get(1).name);
        Assertions.assertEquals("4.7.9-alpha", report.dependencies.get(1).version);
        Assertions.assertEquals(DependencyIntegration.DEPENDENCY, report.dependencies.get(1).integration);
    }

    @Test
    @DisplayName("map parent to dependency: no properties set (null)")
    public void mapParentToDependency_allNull() {
        final var parent = new Parent();
        final var result = DependencyCollector.mapToDependency(parent);
        Assertions.assertNull(result.group);
        Assertions.assertNull(result.name);
        Assertions.assertNull(result.version);
        Assertions.assertEquals(DependencyIntegration.PARENT, result.integration);
    }

    @Test
    @DisplayName("map parent to dependency: example")
    public void mapParentToDependency_values() {
        final var parent = new Parent();
        parent.setGroupId("com.example");
        parent.setArtifactId("my-application");
        parent.setVersion("1.0.0");
        final var result = DependencyCollector.mapToDependency(parent);
        Assertions.assertEquals("com.example", result.group);
        Assertions.assertEquals("my-application", result.name);
        Assertions.assertEquals("1.0.0", result.version);
        Assertions.assertEquals(DependencyIntegration.PARENT, result.integration);
    }

    @Test
    @DisplayName("map plugin to dependency: no properties set (null)")
    public void mapPluginToDependency_allNull() {
        final var plugin = new Plugin();
        plugin.setGroupId(null);
        final var result = DependencyCollector.mapToDependency(plugin);
        Assertions.assertNull(result.group);
        Assertions.assertNull(result.name);
        Assertions.assertNull(result.version);
        Assertions.assertEquals(DependencyIntegration.PLUGIN, result.integration);
    }

    @Test
    @DisplayName("map plugin to dependency: example")
    public void mapPluginToDependency_values() {
        final var plugin = new Plugin();
        plugin.setGroupId("com.example");
        plugin.setArtifactId("my-application");
        plugin.setVersion("1.0.0");
        final var result = DependencyCollector.mapToDependency(plugin);
        Assertions.assertEquals("com.example", result.group);
        Assertions.assertEquals("my-application", result.name);
        Assertions.assertEquals("1.0.0", result.version);
        Assertions.assertEquals(DependencyIntegration.PLUGIN, result.integration);
    }

    @Test
    @DisplayName("map dependency to dependency: no properties set (null)")
    public void mapDependencyToDependency_allNull() {
        final var dependency = new Dependency();
        final var result = DependencyCollector.mapToDependency(dependency);
        Assertions.assertNull(result.group);
        Assertions.assertNull(result.name);
        Assertions.assertNull(result.version);
        Assertions.assertEquals(DependencyIntegration.DEPENDENCY, result.integration);
    }

    @Test
    @DisplayName("map dependency to dependency: example")
    public void mapDependencyToDependency_values() {
        final var dependency = new Dependency();
        dependency.setGroupId("com.example");
        dependency.setArtifactId("my-application");
        dependency.setVersion("1.0.0");
        final var result = DependencyCollector.mapToDependency(dependency);
        Assertions.assertEquals("com.example", result.group);
        Assertions.assertEquals("my-application", result.name);
        Assertions.assertEquals("1.0.0", result.version);
        Assertions.assertEquals(DependencyIntegration.DEPENDENCY, result.integration);
    }

}