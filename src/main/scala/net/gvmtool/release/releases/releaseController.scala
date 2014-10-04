package net.gvmtool.release.releases

import javax.validation.Valid

import net.gvmtool.release.candidate.{CandidateGeneralRepo, CandidateNotFoundException}
import net.gvmtool.release.request.ReleaseRequest
import net.gvmtool.release.validate.Validate
import net.gvmtool.release.version.{Version, VersionRepo}
import net.gvmtool.status.Created
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation._

trait ReleaseController {

  val versionRepo: VersionRepo

  val candidateRepo: CandidateGeneralRepo

  @RequestMapping(value = Array("/release"), method = Array(POST))
  def publish(@Valid @RequestBody request: ReleaseRequest)(implicit binding: BindingResult) =
    Validate {
      Option(candidateRepo.findByCandidate(request.getCandidate)).map { c =>
        Created(versionRepo.save(Version(request)))
      }.getOrElse(throw CandidateNotFoundException(request.getCandidate))
    }

}

@RestController
class Releases @Autowired()(val versionRepo: VersionRepo, val candidateRepo: CandidateGeneralRepo) extends ReleaseController
