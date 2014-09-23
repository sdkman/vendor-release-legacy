package net.gvmtool.release.response

import net.gvmtool.release.version.Version
import org.springframework.http.{HttpStatus, ResponseEntity}

object Ok {
  def apply(version: Version) =
    new ResponseEntity[SuccessResponse](
      new SuccessResponse(version.id.toString), HttpStatus.OK)
}