package net.gvmtool.release.response

import net.gvmtool.release.version.Version
import org.springframework.http.HttpStatus._
import org.springframework.http.ResponseEntity

object Created {
  def apply(v: Version) =
    new ResponseEntity[SuccessResponse](new SuccessResponse(v.id.toString), CREATED)
}

object ServiceUnavailable {
  def apply(m: String) = new ResponseEntity[ErrorResponse](
    new ErrorResponse(SERVICE_UNAVAILABLE.value, m), SERVICE_UNAVAILABLE)
}