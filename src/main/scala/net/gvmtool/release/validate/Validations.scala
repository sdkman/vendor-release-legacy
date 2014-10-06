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
package net.gvmtool.release.validate

import javax.validation.ValidationException

import net.gvmtool.release.{VersionNotFoundException, VersionRepo, Version}
import net.gvmtool.release.request.DefaultVersionRequest
import net.gvmtool.release.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult

object ValidRequest {
  def apply(fun: => ResponseEntity[SuccessResponse])(implicit binding: BindingResult) =
    if (binding.hasErrors) throw new ValidationException(binding.getAllErrors.toString)
    else fun
}

object ValidVersion {
  def apply(f: Version => ResponseEntity[SuccessResponse])(implicit r: DefaultVersionRequest, repo: VersionRepo) = {
    val c = r.getCandidate
    val v = r.getDefaultVersion
    Option(repo.findByCandidateAndVersion(c, v))
      .map(f)
      .getOrElse(throw VersionNotFoundException(c, v))
  }
}