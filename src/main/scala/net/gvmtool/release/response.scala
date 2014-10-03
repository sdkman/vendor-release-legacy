package net.gvmtool

import net.gvmtool.release.response.{SuccessResponse, ErrorResponse}
import org.springframework.http.{ResponseEntity, HttpStatus}

package object response {

  object ErrorResponse {
    def apply(status: Int, message: String) = new ErrorResponse(status, message)
  }

  object SuccessResponse {
    def apply(id: String, message: String) = new SuccessResponse(id, message)
  }
}
