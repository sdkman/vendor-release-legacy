package net.gvmtool.release.response

import net.gvmtool.release.version.Version
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity

object Created {
  def apply(version: Version) =
    new ResponseEntity[SuccessResponse](new SuccessResponse(version.id.toString), CREATED)
}