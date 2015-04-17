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
package net.gvmtool.release

import javax.validation.ValidationException

import net.gvmtool.release.request.ReleaseRequest
import net.gvmtool.release.response._
import org.bson.types.ObjectId
import org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs
import org.junit.runner.RunWith
import org.mockito.Matchers.argThat
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, ShouldMatchers, WordSpec}
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.validation.{BindingResult, ObjectError}

import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class ReleaseControllerSpec extends WordSpec with ShouldMatchers with MockitoSugar with BeforeAndAfter {

  val mockVersionRepo = mock[VersionRepo]
  val mockCandidateRepo = mock[CandidateRepo]

  val token = "1HvHCVcDjUIjJxms8tGTkTdPSngIXqVtWKLluBl9qZ9TtM5AKI"
  val consumer = "groovy"
  val mockBinding = mock[BindingResult]

  before {
    reset(mockVersionRepo, mockCandidateRepo, mockBinding)
  }

  "release controller" should {

    "create a new candidate version" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val version = "2.3.6"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val versionObj = Version(null, candidate, version, url)
      val request = new ReleaseRequest(candidate, version, url)

      val candidateObj = Candidate(new ObjectId("5426b99bba78e60054fe48ca"), candidate, version)
      when(mockCandidateRepo.findByCandidate(candidate)).thenReturn(candidateObj)

      when(mockVersionRepo.findByCandidateAndVersion(candidate, version)).thenReturn(null)

      val persisted = versionObj.copy(id = new ObjectId("54205c4019b02458bdd828db"))
      when(mockVersionRepo.save(argThat[Version](samePropertyValuesAs(versionObj)))).thenReturn(persisted)

      //when
      val response: ResponseEntity[SuccessResponse] = publish(request, token, consumer, mockBinding)

      //then
      response.getStatusCode shouldBe HttpStatus.CREATED
      response.getBody.getId shouldBe "54205c4019b02458bdd828db"
      verify(mockVersionRepo).save(versionObj)
    }

    "reject a duplicate candidate version declaring conflict" in new ControllerUnderTest {
      val candidate = "groovy"
      val version = "2.3.6"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val request = new ReleaseRequest(candidate, version, url)

      val candidateObj = Candidate(new ObjectId("5426b99bba78e60054fe48ca"), candidate, version)
      when(mockCandidateRepo.findByCandidate(candidate)).thenReturn(candidateObj)

      val versionObj = Version(null, candidate, version, url)
      when(mockVersionRepo.findByCandidateAndVersion(candidate, version)).thenReturn(versionObj)

      val e = intercept[DuplicateVersionException] {
        publish(request, token, consumer, mockBinding)
      }

      e.getMessage shouldBe "duplicate candidate version: groovy 2.3.6"
      verify(mockVersionRepo).findByCandidateAndVersion(candidate, version)
    }

    "reject an invalid candidate when releasing declaring bad request" in new ControllerUnderTest {
      //given
      val candidate = "groovee"
      val version = "2.3.7"
      val url = "http://somehost/groovy-binary-2.3.6.zip"

      val request = new ReleaseRequest(candidate, version, url)

      val consumer = "groovee"

      when(mockCandidateRepo.findByCandidate(candidate)).thenReturn(null)

      //when
      val e = intercept[CandidateNotFoundException] {
        publish(request, token, consumer, mockBinding)
      }

      //then
      e.getMessage shouldBe "not a valid candidate: groovee"
      verify(mockCandidateRepo).findByCandidate(candidate)
    }

    "fail validation if field is null" in new ControllerUnderTest {
      val version = "2.3.7"
      val url = "url"
      val request = new ReleaseRequest(null, version, url)

      val consumer = null

      val error = new ObjectError("releaseRequest", "can not be null")
      when(mockBinding.hasErrors).thenReturn(true)
      when(mockBinding.getAllErrors).thenReturn(List[ObjectError](error))

      val e = intercept[ValidationException] {
        publish(request, token, consumer, mockBinding)
      }

      e.getMessage should include("Error in object 'releaseRequest'")
      e.getMessage should include("default message [can not be null]")
    }
    
    "reject access if invalid access token is provided" in new ControllerUnderTest {
      val candidate = "groovy"
      val version = "2.3.6"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val request = new ReleaseRequest(candidate, version, url)

      val token = "invalid_token"

      val e = intercept[AuthorisationDeniedException] {
        publish(request, token, consumer, mockBinding)
      }

      e.getMessage should include("Access prohibited.")
    }

    "reject access if invalid consumer header is provided" in new ControllerUnderTest {
      val consumer = "invalid"

      val candidate = "groovy"
      val version = "2.3.6"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val request = new ReleaseRequest(candidate, version, url)

      val token = "1HvHCVcDjUIjJxms8tGTkTdPSngIXqVtWKLluBl9qZ9TtM5AKI"

      val e = intercept[AuthorisationDeniedException] {
        publish(request, token, consumer, mockBinding)
      }

      e.getMessage should include("Access prohibited.")
    }

    "allow access if the admin consumer header is provided" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val version = "2.3.6"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val versionObj = Version(null, candidate, version, url)
      val request = new ReleaseRequest(candidate, version, url)

      val candidateObj = Candidate(new ObjectId("5426b99bba78e60054fe48ca"), candidate, version)
      when(mockCandidateRepo.findByCandidate(candidate)).thenReturn(candidateObj)

      when(mockVersionRepo.findByCandidateAndVersion(candidate, version)).thenReturn(null)

      val persisted = versionObj.copy(id = new ObjectId("54205c4019b02458bdd828db"))
      when(mockVersionRepo.save(argThat[Version](samePropertyValuesAs(versionObj)))).thenReturn(persisted)

      val adminConsumer = "admin"

      //when
      val response: ResponseEntity[SuccessResponse] = publish(request, token, adminConsumer, mockBinding)

      //then
      response.getStatusCode shouldBe HttpStatus.CREATED
    }
  }

  sealed trait ControllerUnderTest extends ReleaseController {
    val versionRepo = mockVersionRepo
    val candidateRepo = mockCandidateRepo
    val candidateUpdateRepo = mock[CandidateUpdateRepo]
    val secureHeaders = SecureHeaders("1HvHCVcDjUIjJxms8tGTkTdPSngIXqVtWKLluBl9qZ9TtM5AKI", "admin")
  }
}