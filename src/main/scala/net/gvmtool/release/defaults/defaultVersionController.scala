package net.gvmtool.release.defaults

import net.gvmtool.release.Accepted
import net.gvmtool.release.candidate.{Candidate, CandidateGeneralRepo, CandidateUpdateRepo}
import net.gvmtool.release.request.DefaultVersionRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation._

trait DefaultVersionController {

  val candidateUpdateRepo: CandidateUpdateRepo
  val candidateGeneralRepo: CandidateGeneralRepo

  @RequestMapping(value = Array("/default"), method = Array(PUT))
  def default(@RequestBody request: DefaultVersionRequest) =
    Accepted(candidateUpdateRepo.updateDefault(
      Candidate(
        request.getCandidate,
        request.getDefaultVersion)))

}

@RestController
class DefaultVersions @Autowired()(val candidateGeneralRepo: CandidateGeneralRepo, val candidateUpdateRepo: CandidateUpdateRepo) extends DefaultVersionController

class CandidateNotFoundException extends RuntimeException