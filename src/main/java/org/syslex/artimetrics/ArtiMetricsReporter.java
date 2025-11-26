package org.syslex.artimetrics;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.syslex.artimetrics.report.Report;

import java.io.FileReader;
import java.io.IOException;

@Mojo(name = "sayhi")
public class ArtiMetricsReporter extends AbstractMojo {

    @Parameter(property = "artimetrics.server")
    private String server;

    @Parameter(property = "artimetrics.reporting-token")
    private String reportingToken;

    public void execute() {
        getLog().info("Start collecting artifact information for ArtiMetrics.");
        try {
            final var report = buildReport();
            getLog().info("Finished collecting artifact information for ArtiMetrics.");
        } catch (Exception e) {
            getLog().error(e);
            e.printStackTrace();
        }
    }

    static Report buildReport() throws IOException, XmlPullParserException {
        // Initialize the Maven reader
        MavenXpp3Reader reader = new MavenXpp3Reader();

        // Read the pom.xml file
        Model model = reader.read(new FileReader("pom.xml"));

        // build report
        return new ReportBuilder(model).buildReport();
    }

}