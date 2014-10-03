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
