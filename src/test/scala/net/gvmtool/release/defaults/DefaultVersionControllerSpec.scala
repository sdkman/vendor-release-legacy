package net.gvmtool.release.defaults

import net.gvmtool.release.candidate.{Candidate, CandidateRepository}
import net.gvmtool.release.request.CandidateRequest
import net.gvmtool.release.response.SuccessResponse
import org.bson.types.ObjectId
import org.hamcrest.beans.SamePropertyValuesAs._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ShouldMatchers, WordSpec}
import org.springframework.http.{HttpStatus, ResponseEntity}

class DefaultVersionControllerSpec extends WordSpec with ShouldMatchers with MockitoSugar {

  val repository = mock[CandidateRepository]

  "default version controller" should {
    "mark an existing candidate version as default" in new ControllerUnderTest {
      //arrange
      val candidate = "groovy"
      val version = "2.3.6"
      val candidateRequest = new CandidateRequest(candidate, version)

      val candidateObj = Candidate(candidate, version)

      val persisted = candidateObj.copy(id = new ObjectId("5423333bba78831a730c18e2"))
      when(repository.updateDefault(argThat[Candidate](samePropertyValuesAs(candidateObj)))).thenReturn(persisted)

      //act
      val response: ResponseEntity[SuccessResponse] = default(candidateRequest)

      //assert
      response.getStatusCode shouldBe HttpStatus.ACCEPTED
      response.getBody.getId shouldBe "5423333bba78831a730c18e2"
      response.getBody.getMessage shouldBe "default groovy version: 2.3.6"

      verify(repository).updateDefault(argThat[Candidate](samePropertyValuesAs(persisted)))
    }
  }

  trait ControllerUnderTest extends DefaultVersionController {
    val candidateRepo = repository
  }

}
