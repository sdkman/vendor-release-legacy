/**
 * Copyright 2014 Marco Vermeulen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.gvmtool.release.defaults

import javax.validation.ValidationException

import net.gvmtool.release.candidate.{Candidate, CandidateGeneralRepo, CandidateNotFoundException, CandidateUpdateRepo}
import net.gvmtool.release.request.DefaultVersionRequest
import net.gvmtool.release.response.SuccessResponse
import net.gvmtool.release.version.{Version, VersionNotFoundException, VersionRepo}
import org.bson.types.ObjectId
import org.hamcrest.beans.SamePropertyValuesAs._
import org.junit.runner.RunWith
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ShouldMatchers, WordSpec}
import org.springframework.http.HttpStatus._
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.validation.{ObjectError, BindingResult}
import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class DefaultVersionControllerSpec extends WordSpec with ShouldMatchers with MockitoSugar {

  val mockCandidateUpdateRepo = mock[CandidateUpdateRepo]
  val mockCandidateGenRepo = mock[CandidateGeneralRepo]
  val mockVersionRepo = mock[VersionRepo]

  implicit val binding = mock[BindingResult]

  "default version controller" should {
    "mark an existing candidate version as default" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val version = "2.3.6"
      val request = new DefaultVersionRequest(candidate, version)

      val candidateObj = Candidate(candidate, version)
      val persisted = candidateObj.copy(id = new ObjectId("5423333bba78831a730c18e2"))

      val versionFound = Version(
        id = new ObjectId("5426b99bba78e60054fe48ca"), candidate, version,
        url = "http://dl.bintray.com/groovy/maven/groovy-binary-2.3.6.zip")

      when(candidateGenRepo.findByCandidate(candidate)).thenReturn(Candidate(candidate, "2.3.6"))
      when(mockCandidateUpdateRepo.updateDefault(argThat[Candidate](samePropertyValuesAs(candidateObj)))).thenReturn(persisted)
      when(mockVersionRepo.findByCandidateAndVersion(candidate, version)).thenReturn(versionFound)

      //when
      val response: ResponseEntity[SuccessResponse] = default(request)

      //then
      response.getStatusCode shouldBe ACCEPTED
      response.getBody.getId shouldBe "5423333bba78831a730c18e2"
      response.getBody.getMessage shouldBe "default groovy version: 2.3.6"

      verify(mockVersionRepo).findByCandidateAndVersion(candidate, version)
      verify(mockCandidateUpdateRepo).updateDefault(argThat[Candidate](samePropertyValuesAs(persisted)))
    }

    "reject an invalid candidate version as default declaring bad request" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val version = "9.9.9"
      val request = new DefaultVersionRequest(candidate, version)

      when(candidateGenRepo.findByCandidate(candidate)).thenReturn(Candidate(candidate, "2.3.6"))
      when(versionRepo.findByCandidateAndVersion(candidate, version)).thenReturn(null)

      //when
      val e = intercept[VersionNotFoundException] {
        default(request)
      }

      //then
      e.getMessage shouldBe "invalid candidate version: groovy 9.9.9"
      verify(versionRepo).findByCandidateAndVersion(candidate, version)
    }

    "reject invalid candidate as bad request when marking default" in new ControllerUnderTest {
      //given
      val candidate = "groovee"
      val version = "2.3.7"
      val request = new DefaultVersionRequest(candidate, version)

      when(candidateGenRepo.findByCandidate(candidate)).thenReturn(null)

      //when
      val e = intercept[CandidateNotFoundException] {
        default(request)
      }

      //then
      e.getMessage shouldBe "not a valid candidate: groovee"
    }

    "fail validation if field is null" in new ControllerUnderTest {
      val version = "2.3.7"
      val request = new DefaultVersionRequest(null, version)

      val error = new ObjectError("defaultVersionRequest", "can not be null")
      when(binding.hasErrors).thenReturn(true)
      when(binding.getAllErrors).thenReturn(List[ObjectError](error))

      val e = intercept[ValidationException] {
        default(request)
      }

      e.getMessage should include("Error in object 'defaultVersionRequest'")
      e.getMessage should include("default message [can not be null]")
    }
  }

  sealed trait ControllerUnderTest extends DefaultVersionController {
    val candidateUpdateRepo = mockCandidateUpdateRepo
    val versionRepo = mockVersionRepo
    val candidateGenRepo = mockCandidateGenRepo
  }

}
