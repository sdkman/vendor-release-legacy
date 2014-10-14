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
package net.gvmtool.release

import javax.validation.Valid

import net.gvmtool.release.request.DefaultVersionRequest
import net.gvmtool.release.validation.{ValidCandidate, ValidCandidateVersion, ValidRequest}
import net.gvmtool.status.Accepted
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation._

trait DefaultVersionController extends CandidatePersistence with VersionPersistence {
  @RequestMapping(value = Array("/default"), method = Array(PUT))
  def default()(implicit @Valid @RequestBody request: DefaultVersionRequest, binding: BindingResult) =
    ValidRequest {
      ValidCandidate {
        ValidCandidateVersion {
          val candidate = request.getCandidate
          val version = request.getVersion
          Accepted(update(Candidate(candidate, version)))
        }
      }
    }
}

@RestController
class DefaultVersions @Autowired()(val versionRepo: VersionRepo,
                                   val candidateRepo: CandidateRepo,
                                   val candidateUpdateRepo: CandidateUpdateRepo) extends DefaultVersionController