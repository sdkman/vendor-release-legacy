package net.gvmtool.release.defaults

import net.gvmtool.release.candidate.{Candidate, CandidateGeneralRepo, CandidateUpdateRepo}
import net.gvmtool.release.request.DefaultVersionRequest
import net.gvmtool.release.version.VersionRepo
import net.gvmtool.status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation._

trait DefaultVersionController {

  val candidateUpdateRepo: CandidateUpdateRepo

  val candidateGenRepo: CandidateGeneralRepo

  val versionRepo: VersionRepo

  @RequestMapping(value = Array("/default"), method = Array(PUT))
  def default(@RequestBody request: DefaultVersionRequest) = {
    if(candidateGenRepo.findByCandidate(request.getCandidate) == null)
      throw CandidateNotFoundException(s"not a valid candidate: ${request.getCandidate}")
    if(versionRepo.findByCandidateAndVersion(request.getCandidate, request.getDefaultVersion) == null)
      throw VersionNotFoundException(s"invalid candidate version: ${request.getCandidate} ${request.getDefaultVersion}")
    status.Accepted(candidateUpdateRepo.updateDefault(
      Candidate(
        request.getCandidate,
        request.getDefaultVersion)))
  }

  @ExceptionHandler
  def handle(e: VersionNotFoundException) = status.BadRequest(e.getMessage)

  @ExceptionHandler
  def handle(e: CandidateNotFoundException) = status.BadRequest(e.getMessage)

}

@RestController
class DefaultVersions @Autowired()(val versionRepo: VersionRepo, val candidateGenRepo: CandidateGeneralRepo, val candidateUpdateRepo: CandidateUpdateRepo) extends DefaultVersionController

class VersionNotFoundException(message: String) extends RuntimeException(message: String)

object VersionNotFoundException {
  def apply(m: String) = new VersionNotFoundException(m)
}

class CandidateNotFoundException(message: String) extends RuntimeException(message: String)

object CandidateNotFoundException {
  def apply(m: String) = new CandidateNotFoundException(m)
}