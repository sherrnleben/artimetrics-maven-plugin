package org.syslex.artimetrics.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Programming {

    @JsonProperty
    public Language language;

    @JsonProperty
    public String version;

}
