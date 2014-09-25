package net.gvmtool.release.releases

import net.gvmtool.release._
import net.gvmtool.release.request.VersionRequest
import net.gvmtool.release.version.{Version, VersionRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation._

trait ReleaseController {

  val repo: VersionRepository

  @RequestMapping(value = Array("/release"), method = Array(POST))
  def publish(@RequestBody request: VersionRequest) = Created(repo.save(Version(request)))

  @ExceptionHandler
  def handle(e: DataAccessException) = ServiceUnavailable(e.getMessage)

}

@RestController
class Releases @Autowired()(val repo: VersionRepository) extends ReleaseController
