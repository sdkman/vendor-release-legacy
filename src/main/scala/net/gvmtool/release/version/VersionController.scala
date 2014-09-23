package net.gvmtool.release.version

import net.gvmtool.release.request.VersionRequest
import net.gvmtool.release.response.Ok

trait VersionController {

  val repo: VersionRepository

  def publish(request: VersionRequest) = Ok(repo.save(Version(request)))

}
