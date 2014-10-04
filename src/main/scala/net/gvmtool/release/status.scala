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
package net.gvmtool

import net.gvmtool.release.candidate.Candidate
import net.gvmtool.release.version.Version
import net.gvmtool.response.{ErrorResponse, SuccessResponse}
import org.springframework.http.HttpStatus._
import org.springframework.http.ResponseEntity

package object status {
  def Accepted(c: Candidate) =
    new ResponseEntity(
      SuccessResponse(c.id.toString, s"default ${c.candidate} version: ${c.default}"), ACCEPTED)

  def BadRequest(m: String) =
    new ResponseEntity(
      ErrorResponse(BAD_REQUEST.value, m), BAD_REQUEST)

  def Created(v: Version) =
    new ResponseEntity(
      SuccessResponse(v.id.toString, s"released ${v.candidate} version: ${v.version}"), CREATED)

  def ServiceUnavailable(m: String) =
    new ResponseEntity(
      ErrorResponse(SERVICE_UNAVAILABLE.value, m), SERVICE_UNAVAILABLE)
}
