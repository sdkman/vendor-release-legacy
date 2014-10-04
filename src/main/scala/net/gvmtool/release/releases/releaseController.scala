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
package net.gvmtool.release.releases

import javax.validation.Valid

import net.gvmtool.release.candidate.{CandidateGeneralRepo, CandidateNotFoundException}
import net.gvmtool.release.request.ReleaseRequest
import net.gvmtool.release.validate.Validate
import net.gvmtool.release.version.{Version, VersionRepo}
import net.gvmtool.status.Created
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation._

trait ReleaseController {

  val versionRepo: VersionRepo

  val candidateRepo: CandidateGeneralRepo

  @RequestMapping(value = Array("/release"), method = Array(POST))
  def publish(@Valid @RequestBody request: ReleaseRequest)(implicit binding: BindingResult) =
    Validate {
      Option(candidateRepo.findByCandidate(request.getCandidate)).map { c =>
        Created(versionRepo.save(Version(request)))
      }.getOrElse(throw CandidateNotFoundException(request.getCandidate))
    }

}

@RestController
class Releases @Autowired()(val versionRepo: VersionRepo, val candidateRepo: CandidateGeneralRepo) extends ReleaseController
