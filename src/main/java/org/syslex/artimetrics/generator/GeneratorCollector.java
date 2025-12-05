package org.syslex.artimetrics.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.syslex.artimetrics.report.Generator;
import org.syslex.artimetrics.report.Report;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Properties;

/**
 * Collector for information about report generation
 */
public class GeneratorCollector implements Collector {

    private final static Logger logger = LoggerFactory.getLogger(GeneratorCollector.class);

    @Override
    public void supply(final Report report) {
        // ensure that the report object contains the generator object
        if (Objects.isNull(report.generator))
            report.generator = new Generator();

        final var properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
        } catch (final IOException e) {
            logger.warn("Error loading project properties file to extract generator name and version", e);
        }

        // set values for generator
        report.generator.name = properties.getProperty("artifactId");
        report.generator.version = properties.getProperty("version");
        report.generator.generatedAt = OffsetDateTime.now();
    }
}
