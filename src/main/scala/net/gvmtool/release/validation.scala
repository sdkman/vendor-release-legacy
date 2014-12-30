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

import javax.validation.ValidationException

import net.gvmtool.release.request.SimpleRequest
import net.gvmtool.release.response._
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult

object ValidRequest {
  def apply(fun: => ResponseEntity[SuccessResponse])(implicit binding: BindingResult) =
    if (binding.hasErrors) throw new ValidationException(binding.getAllErrors.toString)
    else fun
}

trait EntityValidation {
  def validCandidate(c: String)(implicit repo: CandidateRepo): String =
    Option(repo.findByCandidate(c)).fold(throw CandidateNotFoundException(c))(c => c.candidate)

  def validVersion(c: String, v: String)(implicit repo: VersionRepo): String =
    Option(repo.findByCandidateAndVersion(c, v)).fold(throw VersionNotFoundException(c, v))(v => v.version)

  def uniqueVersion(c: String, v: String)(implicit repo: VersionRepo): String =
    Option(repo.findByCandidateAndVersion(c, v))
      .map(version => throw DuplicateVersionException(version.candidate, version.version))
      .getOrElse(v)
}

object ValidCandidateVersion {
  def apply(fun: => ResponseEntity[SuccessResponse])(implicit repo: VersionRepo, request: SimpleRequest) = {
    Option(repo.findByCandidateAndVersion(request.getCandidate, request.getVersion))
      .map(v => fun)
      .getOrElse(throw VersionNotFoundException(request.getCandidate, request.getVersion))
  }
}

object ValidCandidate {
  def apply(fun: => ResponseEntity[SuccessResponse])(implicit repo: CandidateRepo, request: SimpleRequest) = {
    Option(repo.findByCandidate(request.getCandidate))
      .map(c => fun)
      .getOrElse(throw CandidateNotFoundException(request.getCandidate))
  }
}
