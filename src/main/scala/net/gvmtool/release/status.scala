package net.gvmtool

import net.gvmtool.release.candidate.Candidate
import net.gvmtool.release.response.{ErrorResponse, SuccessResponse}
import net.gvmtool.release.version.Version
import org.springframework.http.HttpStatus._
import org.springframework.http.ResponseEntity

package object status {
  def Accepted(c: Candidate) =
    new ResponseEntity[SuccessResponse](new SuccessResponse(c.id.toString, s"default ${c.candidate} version: ${c.default}"), ACCEPTED)

  def Created(v: Version) =
    new ResponseEntity[SuccessResponse](new SuccessResponse(v.id.toString, s"released ${v.candidate} version: ${v.version}"), CREATED)

  def ServiceUnavailable(m: String) =
    new ResponseEntity[ErrorResponse](new ErrorResponse(SERVICE_UNAVAILABLE.value, m), SERVICE_UNAVAILABLE)
}
