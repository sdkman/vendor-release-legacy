package net.gvmtool.release.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class CandidateRequest {
    @NotBlank(message = "Candidate name must not be blank.")
    private String candidate;

    @NotBlank(message = "Version must be given.")
    private String version;

    @NotBlank(message = "URL must be provided.")
    private String url;

    public CandidateRequest() {
        //needs default constructor
    }

    @JsonCreator
    public CandidateRequest(@JsonProperty("candidate") String candidate,
            @JsonProperty("version") String version,
            @JsonProperty("url") String url) {
        this.candidate = candidate;
        this.version = version;
        this.url = url;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "CandidateRequest{" +
                "candidate='" + candidate + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
