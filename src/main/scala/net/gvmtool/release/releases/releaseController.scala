package net.gvmtool.release.releases

import net.gvmtool.release._
import net.gvmtool.release.request.ReleaseRequest
import net.gvmtool.release.version.{Version, VersionRepo}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation._

trait ReleaseController {

  val repo: VersionRepo

  @RequestMapping(value = Array("/release"), method = Array(POST))
  def publish(@RequestBody request: ReleaseRequest) = Created(repo.save(Version(request)))

  @ExceptionHandler
  def handle(e: DataAccessException) = ServiceUnavailable(e.getMessage)

}

@RestController
class Releases @Autowired()(val repo: VersionRepo) extends ReleaseController