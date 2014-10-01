package net.gvmtool.release.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class DefaultVersionRequest {
    @NotBlank(message = "Candidate name must not be blank.")
    private String candidate;

    @NotBlank(message = "Version must be provided.")
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
