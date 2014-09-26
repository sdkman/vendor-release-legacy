package net.gvmtool.release.defaults

import net.gvmtool.release.Accepted
import net.gvmtool.release.candidate.{Candidate, CandidateRepository}
import net.gvmtool.release.request.CandidateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation._

trait DefaultVersionController {

  val candidateRepo: CandidateRepository

  @RequestMapping(value = Array("/default"), method = Array(PUT))
  def default(@RequestBody request: CandidateRequest) =
    Accepted(candidateRepo.updateDefault(
      Candidate(
        request.getCandidate,
        request.getDefaultVersion)),
      request.getDefaultVersion)

}

@RestController
class DefaultVersions @Autowired()(val candidateRepo: CandidateRepository) extends DefaultVersionController