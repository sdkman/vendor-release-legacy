package net.gvmtool.release.releases

import net.gvmtool.release.candidate.{Candidate, CandidateGeneralRepo, CandidateNotFoundException}
import net.gvmtool.release.request.ReleaseRequest
import net.gvmtool.release.response.SuccessResponse
import net.gvmtool.release.version.{Version, VersionRepo}
import org.bson.types.ObjectId
import org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs
import org.junit.runner.RunWith
import org.mockito.Matchers.argThat
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ShouldMatchers, WordSpec}
import org.springframework.http.{HttpStatus, ResponseEntity}

@RunWith(classOf[JUnitRunner])
class ReleaseControllerSpec extends WordSpec with ShouldMatchers with MockitoSugar {

  val mockVersionRepo = mock[VersionRepo]
  val mockCandidateRepo = mock[CandidateGeneralRepo]

  "release controller" should {
    "create a new candidate version" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val version = "2.3.6"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val request = new ReleaseRequest(candidate, version, url)
      val versionObj = Version(null, candidate, version, url)

      val candidateObj = Candidate(new ObjectId("5426b99bba78e60054fe48ca"), candidate, version)
      when(mockCandidateRepo.findByCandidate(candidate)).thenReturn(candidateObj)

      val persisted = versionObj.copy(id = new ObjectId("54205c4019b02458bdd828db"))
      when(mockVersionRepo.save(argThat[Version](samePropertyValuesAs(versionObj)))).thenReturn(persisted)

      //when
      val response: ResponseEntity[SuccessResponse] = publish(request)

      //then
      response.getStatusCode shouldBe HttpStatus.CREATED
      response.getBody.getId shouldBe "54205c4019b02458bdd828db"
      verify(mockVersionRepo).save(versionObj)
    }

    "reject an invalid candidate when releasing declaring bad request" in new ControllerUnderTest {
      //given
      val candidate = "groovee"
      val version = "2.3.7"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val request = new ReleaseRequest(candidate, version, url)

      when(mockCandidateRepo.findByCandidate(candidate)).thenReturn(null)

      //when
      val e = intercept[CandidateNotFoundException] {
        publish(request)
      }

      //then
      e.getMessage shouldBe "not a valid candidate: groovee"
      verify(mockCandidateRepo).findByCandidate(candidate)
    }
  }

  sealed trait ControllerUnderTest extends ReleaseController {
    val versionRepo = mockVersionRepo
    val candidateRepo = mockCandidateRepo
  }

}