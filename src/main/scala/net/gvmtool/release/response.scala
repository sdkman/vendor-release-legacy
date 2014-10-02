package net.gvmtool

import net.gvmtool.release.response.{SuccessResponse, ErrorResponse}
import org.springframework.http.{ResponseEntity, HttpStatus}

package object response {

  object ErrorResponse {
    def apply(status: HttpStatus, message: String) = new ErrorResponse(status.value, message)
  }

  object SuccessResponse {
    def apply(id: String, message: String) = new SuccessResponse(id, message)
  }

  object ResponseEntity {
    def apply[T](t: T, s: HttpStatus) = new ResponseEntity[T](t, s)
  }
}
