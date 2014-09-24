package net.gvmtool.release.version

import net.gvmtool.release.request.VersionRequest
import net.gvmtool.release.response.responses._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation._

trait VersionController {

  val repo: VersionRepository

  @RequestMapping(value = Array("/release"), method = Array(POST))
  def publish(@RequestBody request: VersionRequest) = Created(repo.save(Version(request)))

  @ExceptionHandler
  def handle(e: DataAccessException) = ServiceUnavailable(e.getMessage)

}

@RestController
class Versions @Autowired()(val repo: VersionRepository) extends VersionController
