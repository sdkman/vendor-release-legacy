package net.gvmtool.release.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class DefaultVersionRequest {
    @NotNull(message = "Candidate name can not be null.")
    private String candidate;

    @NotNull(message = "Default Version can not be null.")
    private String defaultVersion;

    public DefaultVersionRequest() {
        //needs default constructor
    }

    @JsonCreator
    public DefaultVersionRequest(@JsonProperty("candidate") String candidate,
                                 @JsonProperty("default") String defaultVersion) {
        this.candidate = candidate;
        this.defaultVersion = defaultVersion;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getDefaultVersion() {
        return defaultVersion;
    }

    @Override
    public String toString() {
        return "CandidateRequest{" +
                "candidate='" + candidate + '\'' +
                ", defaultVersion='" + defaultVersion + '\'' +
                '}';
    }
}
