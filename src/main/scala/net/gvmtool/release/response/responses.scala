package net.gvmtool.release.response

import net.gvmtool.release.version.Version
import org.springframework.http.HttpStatus._
import org.springframework.http.ResponseEntity

package object responses {
  def Created(v: Version) =
    new ResponseEntity[SuccessResponse](new SuccessResponse(v.id.toString), CREATED)

  def ServiceUnavailable(m: String) =
    new ResponseEntity[ErrorResponse](new ErrorResponse(SERVICE_UNAVAILABLE.value, m), SERVICE_UNAVAILABLE)
}
