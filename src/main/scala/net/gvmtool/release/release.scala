package net.gvmtool

import net.gvmtool.release.response.{ErrorResponse, SuccessResponse}
import net.gvmtool.release.version.Version
import org.springframework.http.HttpStatus._
import org.springframework.http.ResponseEntity

package object release {
  def Created(v: Version) =
    new ResponseEntity[SuccessResponse](new SuccessResponse(v.id.toString), CREATED)

  def ServiceUnavailable(m: String) =
    new ResponseEntity[ErrorResponse](new ErrorResponse(SERVICE_UNAVAILABLE.value, m), SERVICE_UNAVAILABLE)
}
