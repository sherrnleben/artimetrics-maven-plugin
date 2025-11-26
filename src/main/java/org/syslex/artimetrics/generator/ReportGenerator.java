package org.syslex.artimetrics.generator;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.syslex.artimetrics.report.Generator;
import org.syslex.artimetrics.report.Report;

import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Properties;

public class ReportGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);
    private final Model pomModel;

    private ReportGenerator(Model pomModel) {
        this.pomModel = pomModel;
    }

    public static ReportGenerator init() throws IOException, XmlPullParserException {
        // Initialize the Maven reader
        final var reader = new MavenXpp3Reader();

        // Read the pom.xml file
        final var filename = "pom.xml";
        final var pomModel = reader.read(new FileReader(filename));
        logger.info("Read project object model from {}", filename);

        // instantiate generator
        return new ReportGenerator(pomModel);
    }

    static ReportGenerator init(final Model pomModel) {
        return new ReportGenerator(pomModel);
    }

    public Report generateReport() {
        final var report = new Report();
        collectGenerator(report);
        return report;
    }

    /**
     * Collect information about report generation and add it to the generated report
     *
     * @param report the report to which the generator information should be added
     */
    void collectGenerator(final Report report) {
        // ensure that the report object contains the generator object
        if (Objects.isNull(report.generator))
            report.generator = new Generator();

        final Properties properties = new Properties();
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
