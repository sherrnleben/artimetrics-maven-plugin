package org.syslex.artimetrics.generator;

import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.syslex.artimetrics.report.Language;
import org.syslex.artimetrics.report.Programming;
import org.syslex.artimetrics.report.Report;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Collector for programming language
 */
public class ProgrammingCollector implements Collector {

    private final Model model;

    public ProgrammingCollector(final Model model) {
        this.model = model;
    }

    @Override
    public void supply(final Report report) {
        // ensure that report contains programming object
        if (Objects.isNull(report.language))
            report.language = new Programming();

        // set values for programming
        report.language.language = Language.Java;
        report.language.version = Stream.of(
                        readJavaVersionFromReleaseProperties(model, "maven.compiler.release"),
                        readJavaVersionFromReleaseProperties(model, "maven.compiler.target"),
                        readJavaVersionFromReleaseProperties(model, "maven.compiler.source"),
                        readJavaVersionFromMavenCompilerConfig(model, "release"),
                        readJavaVersionFromMavenCompilerConfig(model, "target"),
                        readJavaVersionFromMavenCompilerConfig(model, "source")
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(null);
    }

    /**
     * Read Java version from POM properties (release)
     *
     * @param model       Maven POM model
     * @param propertyKey Key of property
     * @return Optional of Java version
     */
    Optional<String> readJavaVersionFromReleaseProperties(final Model model, final String propertyKey) {
        return Objects.nonNull(model.getProperties()) && model.getProperties().containsKey(propertyKey)
                ? Optional.of(model.getProperties().getProperty(propertyKey))
                : Optional.empty();
    }

    /**
     * Read Java version from Maven properties (release)
     *
     * @param model     Maven POM model
     * @param configKey Key of configuration
     * @return Optional of Java version
     */
    Optional<String> readJavaVersionFromMavenCompilerConfig(final Model model, final String configKey) {
        if (Objects.isNull(model.getBuild()) || Objects.isNull(model.getBuild().getPlugins()))
            return Optional.empty();

        // find maven compiler plugin
        final var compilerPlugin = model.getBuild().getPlugins().stream()
                .filter(plugin -> Objects.nonNull(plugin.getArtifactId()))
                .filter(plugin -> plugin.getArtifactId().equals("maven-compiler-plugin"))
                .findFirst();
        if (compilerPlugin.isEmpty()
                || Objects.isNull(compilerPlugin.get().getConfiguration())
                || !(compilerPlugin.get().getConfiguration() instanceof Xpp3Dom config))
            return Optional.empty();

        final var param = config.getChild(configKey);
        if (Objects.isNull(param) || Objects.isNull(param.getValue()))
            return Optional.empty();

        return Optional.of(param.getValue());
    }
}
