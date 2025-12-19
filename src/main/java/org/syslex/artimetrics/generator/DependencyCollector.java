package org.syslex.artimetrics.generator;

import org.apache.maven.model.Model;
import org.syslex.artimetrics.report.Dependency;
import org.syslex.artimetrics.report.DependencyIntegration;
import org.syslex.artimetrics.report.Report;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Collector for included project dependencies
 */
public class DependencyCollector implements Collector {

    private final Model model;

    public DependencyCollector(final Model model) {
        this.model = model;
    }

    /**
     * Map a dependency model from POM to the generic report dependency model
     *
     * @param modelDependency Dependency model from POM
     * @return Generic dependency report model
     */
    public static Dependency mapToDependency(final org.apache.maven.model.Dependency modelDependency) {
        final var dependency = new Dependency();
        dependency.group = modelDependency.getGroupId();
        dependency.name = modelDependency.getArtifactId();
        dependency.version = modelDependency.getVersion();
        dependency.integration = DependencyIntegration.DEPENDENCY;
        return dependency;
    }

    /**
     * Map a plugin model from POM to the generic report dependency model
     *
     * @param plugin Plugin model from POM
     * @return Generic dependency report mode
     */
    static Dependency mapToDependency(final org.apache.maven.model.Plugin plugin) {
        final var dependency = new Dependency();
        dependency.group = plugin.getGroupId();
        dependency.name = plugin.getArtifactId();
        dependency.version = plugin.getVersion();
        dependency.integration = DependencyIntegration.PLUGIN;
        return dependency;
    }

    /**
     * Map a parent model from POM to the generic report dependency model
     *
     * @param parent Parent model from POM
     * @return Generic dependency report mode
     */
    static Dependency mapToDependency(final org.apache.maven.model.Parent parent) {
        final var dependency = new Dependency();
        dependency.group = parent.getGroupId();
        dependency.name = parent.getArtifactId();
        dependency.version = parent.getVersion();
        dependency.integration = DependencyIntegration.PARENT;
        return dependency;
    }

    @Override
    public void supply(final Report report) {
        // ensure that the report contains a list for collected dependencies
        if (Objects.isNull(report.dependencies))
            report.dependencies = new ArrayList<>();

        collectParent(report);
        collectPlugins(report);
        collectDependencies(report);

    }

    /**
     * Collect information about the parent
     */
    void collectParent(final Report report) {

        // abort if no parent is found
        if (Objects.isNull(model.getParent()))
            return;

        // map parent to generic dependency and add it to the report
        report.dependencies.add(mapToDependency(model.getParent()));
    }

    /**
     * Collect information about plugins
     */
    void collectPlugins(final Report report) {
        // abort if no plugins are found
        if (Objects.isNull(model.getBuild()) || Objects.isNull(model.getBuild().getPlugins()) || model.getBuild().getPlugins().isEmpty())
            return;

        // stream over all plugins, map them to generic dependency, and add them to the report
        model.getBuild().getPlugins().stream()
                .map(DependencyCollector::mapToDependency)
                .forEach(report.dependencies::add);
    }

    /**
     * Collect information about dependencies and add it to the report
     */
    void collectDependencies(final Report report) {
        // abort if no dependencies are found
        if (Objects.isNull(model.getDependencies()) || model.getDependencies().isEmpty())
            return;

        // stream over all dependencies, map them to generic dependency, and add them to the report
        model.getDependencies().stream()
                .peek(dependency -> System.out.println(dependency.getGroupId() + ":" + dependency.getArtifactId()))
                .map(DependencyCollector::mapToDependency)
                .forEach(report.dependencies::add);
    }
}
