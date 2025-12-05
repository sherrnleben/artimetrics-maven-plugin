package org.syslex.artimetrics.generator;

import org.syslex.artimetrics.report.Report;

public interface Collector {

    /**
     * Collect the data and inject them into the given report
     *
     * @param report The report in that the data should be injected
     */
    void supply(final Report report);
}
