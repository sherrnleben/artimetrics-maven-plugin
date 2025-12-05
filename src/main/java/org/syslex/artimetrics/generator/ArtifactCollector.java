package org.syslex.artimetrics.generator;

import org.apache.maven.model.Model;
import org.syslex.artimetrics.report.ArtifactIdentity;
import org.syslex.artimetrics.report.Report;

import java.util.Objects;

/**
 * Collector for artifact identity, such as artifact name and version
 */
public class ArtifactCollector implements Collector {

    private final Model model;

    public ArtifactCollector(Model model) {
        this.model = model;
    }

    @Override
    public void supply(Report report) {
        // ensure that report contains artifact object
        if (Objects.isNull(report.artifact))
            report.artifact = new ArtifactIdentity();

        // set values for artifact
        report.artifact.group = model.getGroupId();
        report.artifact.name = model.getArtifactId();
        report.artifact.version = model.getVersion();
    }
}
