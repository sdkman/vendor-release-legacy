package net.gvmtool.release.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ReleaseRequest {

    @NotNull(message = "Candidate can not be null.")
    private String candidate;

    @NotNull(message = "Version can not be null.")
    private String version;

    @NotNull(message = "URL can not be null.")
    private String url;

    public ReleaseRequest() {
        //do not remove
    }

    @JsonCreator
    public ReleaseRequest(
            @JsonProperty("candidate") String candidate,
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
        return "VersionRequest{" +
                "candidate='" + candidate + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
