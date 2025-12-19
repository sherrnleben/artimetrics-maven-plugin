package org.syslex.artimetrics.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dependency extends ArtifactIdentity {

    @JsonProperty
    public DependencyIntegration integration;

}
