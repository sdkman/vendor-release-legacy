package net.gvmtool.release.defaults

import javax.validation.Valid

import net.gvmtool.release.candidate.{Candidate, CandidateGeneralRepo, CandidateNotFoundException, CandidateUpdateRepo}
import net.gvmtool.release.request.DefaultVersionRequest
import net.gvmtool.release.validate.Validate
import net.gvmtool.release.version.{VersionNotFoundException, VersionRepo}
import net.gvmtool.status.Accepted
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation._

trait DefaultVersionController {

  val candidateUpdateRepo: CandidateUpdateRepo

  val candidateGenRepo: CandidateGeneralRepo

  val versionRepo: VersionRepo

  @RequestMapping(value = Array("/default"), method = Array(PUT))
  def default(@Valid @RequestBody request: DefaultVersionRequest)(implicit binding: BindingResult) = Validate {
    val candidate = request.getCandidate
    val version = request.getDefaultVersion
    Option(candidateGenRepo.findByCandidate(candidate)).map { c =>
      Option(versionRepo.findByCandidateAndVersion(c.candidate, version)).map { v =>
        Accepted(candidateUpdateRepo.updateDefault(Candidate(v.candidate, v.version)))
      }.getOrElse(throw VersionNotFoundException(candidate, version))
    }.getOrElse(throw CandidateNotFoundException(candidate))
  }
}

@RestController
class DefaultVersions @Autowired()(val versionRepo: VersionRepo, val candidateGenRepo: CandidateGeneralRepo, val candidateUpdateRepo: CandidateUpdateRepo) extends DefaultVersionController