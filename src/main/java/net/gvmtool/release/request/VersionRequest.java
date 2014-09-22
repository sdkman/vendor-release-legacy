package net.gvmtool.release.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VersionRequest {
    private String candidate;
    private String version;
    private String url;

    public VersionRequest() {
        //do not remove
    }

    @JsonCreator
    public VersionRequest(
            @JsonProperty("candidate") String candidate,
            @JsonProperty("versions") String version,
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
