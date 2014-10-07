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
package net.gvmtool.release.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class DefaultVersionRequest implements SimpleRequest {
    @NotNull(message = "Candidate name can not be null.")
    private String candidate;

    @NotNull(message = "Default Version can not be null.")
    private String version;

    public DefaultVersionRequest() {
        //needs default constructor
    }

    @JsonCreator
    public DefaultVersionRequest(@JsonProperty("candidate") String candidate,
                                 @JsonProperty("default") String version) {
        this.candidate = candidate;
        this.version = version;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "CandidateRequest{" +
                "candidate='" + candidate + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
