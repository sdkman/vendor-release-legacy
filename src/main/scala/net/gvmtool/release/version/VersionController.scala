package net.gvmtool.release.version

import net.gvmtool.release.request.VersionRequest
import net.gvmtool.release.response.Ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation._

trait VersionController {

  val repo: VersionRepository

  @RequestMapping(value = Array("/release"), method = Array(POST))
  def publish(@RequestBody request: VersionRequest) = Ok(repo.save(Version(request)))

}

@RestController
class Versions @Autowired()(val repo: VersionRepository) extends VersionController
