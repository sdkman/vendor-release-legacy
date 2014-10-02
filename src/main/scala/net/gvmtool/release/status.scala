package net.gvmtool

import net.gvmtool.release.candidate.Candidate
import net.gvmtool.release.version.Version
import net.gvmtool.response.{ErrorResponse, ResponseEntity, SuccessResponse}
import org.springframework.http.HttpStatus._

package object status {
  def Accepted(c: Candidate) =
    ResponseEntity(
      SuccessResponse(c.id.toString, s"default ${c.candidate} version: ${c.default}"), ACCEPTED)

  def BadRequest(m: String) =
    ResponseEntity(
      ErrorResponse(BAD_REQUEST, m), BAD_REQUEST)

  def Created(v: Version) =
    ResponseEntity(
      SuccessResponse(v.id.toString, s"released ${v.candidate} version: ${v.version}"), CREATED)

  def ServiceUnavailable(m: String) =
    ResponseEntity(
      ErrorResponse(SERVICE_UNAVAILABLE, m), SERVICE_UNAVAILABLE)
}
