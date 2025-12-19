package org.syslex.artimetrics;

import org.apache.maven.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.syslex.artimetrics.report.Report;

public class ReportBuilder {

    private final static Logger logger = LoggerFactory.getLogger(ReportBuilder.class);

    private final Model model;
    Report report;

    public ReportBuilder(final Model model) {
        this.model = model;
    }


    public Report buildReport() {
        report = new Report();
        return report;
    }



}
