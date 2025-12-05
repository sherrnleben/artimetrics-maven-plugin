package org.syslex.artimetrics;

import org.apache.maven.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.syslex.artimetrics.report.Dependency;
import org.syslex.artimetrics.report.DependencyInclusion;
import org.syslex.artimetrics.report.Report;

import java.util.ArrayList;
import java.util.Objects;

public class ReportBuilder {

    private final static Logger logger = LoggerFactory.getLogger(ReportBuilder.class);

    private final Model model;
    Report report;

    public ReportBuilder(final Model model) {
        this.model = model;
    }

    /**
     * Map dependency model from POM to generic dependency model of report
     *
     * @param modelDependency Dependency model from POM
     * @return Generic dependency report mode
     */
    public static Dependency mapToDependency(final org.apache.maven.model.Dependency modelDependency) {
        final var dependency = new Dependency();
        dependency.group = modelDependency.getGroupId();
        dependency.name = modelDependency.getArtifactId();
        dependency.version = modelDependency.getVersion();
        dependency.inclusion = DependencyInclusion.DEPENDENCY;
        return dependency;
    }

    /**
     * Map plugin model from POM to generic dependency model of report
     *
     * @param plugin Plugin model from POM
     * @return Generic dependency report mode
     */
    public static Dependency mapToDependency(final org.apache.maven.model.Plugin plugin) {
        final var dependency = new Dependency();
        dependency.group = plugin.getGroupId();
        dependency.name = plugin.getArtifactId();
        dependency.version = plugin.getVersion();
        dependency.inclusion = DependencyInclusion.PLUGIN;
        return dependency;
    }

    /**
     * Map parent model from POM to generic dependency model of report
     *
     * @param parent Parent model from POM
     * @return Generic dependency report mode
     */
    public static Dependency mapToDependency(final org.apache.maven.model.Parent parent) {
        final var dependency = new Dependency();
        dependency.group = parent.getGroupId();
        dependency.name = parent.getArtifactId();
        dependency.version = parent.getVersion();
        dependency.inclusion = DependencyInclusion.PARENT;
        return dependency;
    }

    public Report buildReport() {
        report = new Report();
        collectParent();
        collectDependencies();
        collectPlugins();
        return report;
    }

    /**
     * Collect information about parent and add it to report
     */
    void collectParent() {
        // ensure that report contains a list for collected dependencies
        if (Objects.isNull(report.dependencies))
            report.dependencies = new ArrayList<>();

        // abort if no parent is found
        if (Objects.isNull(model.getParent()))
            return;

        // map parent to generic dependency and add it to report
        report.dependencies.add(ReportBuilder.mapToDependency(model.getParent()));
    }

    /**
     * Collect information about plugins and add it to report
     */
    void collectPlugins() {
        // ensure that report contains a list for collected dependencies
        if (Objects.isNull(report.dependencies))
            report.dependencies = new ArrayList<>();

        // abort if no plugins are found
        if (Objects.isNull(model.getBuild()) || Objects.isNull(model.getBuild().getPlugins()))
            return;

        // stream over all plugins, map them to generic dependency, and add them to report
        model.getBuild().getPlugins().stream()
                .map(ReportBuilder::mapToDependency)
                .forEach(report.dependencies::add);
    }

    /**
     * Collect information about dependencies and add it to report
     */
    void collectDependencies() {
        // ensure that report contains a list for collected dependencies
        if (Objects.isNull(report.dependencies))
            report.dependencies = new ArrayList<>();

        // abort if no dependencies are found
        if (Objects.isNull(model.getDependencies()))
            return;

        // stream over all dependencies, map them to generic dependency, and add them to report
        model.getDependencies().stream()
                .map(ReportBuilder::mapToDependency)
                .forEach(report.dependencies::add);
    }

}
