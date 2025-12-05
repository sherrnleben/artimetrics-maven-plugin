package org.syslex.artimetrics.generator;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.syslex.artimetrics.report.Report;

import java.io.FileReader;
import java.io.IOException;

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
        new GeneratorCollector().supply(report);
        new ProgrammingCollector(pomModel).supply(report);
        return report;
    }

}
