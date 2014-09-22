package net.gvmtool.release.version

import net.gvmtool.release.request.VersionRequest
import net.gvmtool.release.response.SuccessResponse
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

trait VersionController {

  val repo: VersionRepository

  def publish(request: VersionRequest): ResponseEntity[SuccessResponse] =
    new ResponseEntity[SuccessResponse](
      new SuccessResponse(
        repo.save(Version(request)).id.toString), OK)
}
