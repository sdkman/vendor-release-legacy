/**
 * Copyright 2014 Marco Vermeulen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sdkman.release.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ReleaseRequest implements SimpleRequest {

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
