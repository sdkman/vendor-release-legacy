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

import net.gvmtool.release.request.DefaultVersionRequest
import net.gvmtool.release.response._
import org.bson.types.ObjectId
import org.hamcrest.beans.SamePropertyValuesAs._
import org.junit.runner.RunWith
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, ShouldMatchers, WordSpec}
import org.springframework.http.HttpStatus._
import org.springframework.http.ResponseEntity
import org.springframework.validation.{BindingResult, ObjectError}

import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class DefaultVersionControllerSpec extends WordSpec with ShouldMatchers with BeforeAndAfter with MockitoSugar {

  val mockCandidateUpdateRepo = mock[CandidateUpdateRepo]
  val mockCandidateRepo = mock[CandidateRepo]
  val mockVersionRepo = mock[VersionRepo]

  val token = "1HvHCVcDjUIjJxms8tGTkTdPSngIXqVtWKLluBl9qZ9TtM5AKI"
  val consumer = "groovy"
  val mockBinding = mock[BindingResult]

  before {
    reset(mockCandidateUpdateRepo, mockCandidateRepo, mockVersionRepo, mockBinding)
  }

  "default version controller" should {
    "mark an existing candidate version as default" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val oldVersion = "2.3.5"
      val newVersion = "2.3.6"

      val request = new DefaultVersionRequest(candidate, newVersion)

      val candidateObj = Candidate(candidate, oldVersion)
      when(candidateRepo.findByCandidate(candidate)).thenReturn(candidateObj)

      val versionObj = Version(
          id = new ObjectId("5426b99bba78e60054fe48ca"), candidate, newVersion,
          url = "http://dl.bintray.com/groovy/maven/groovy-binary-2.3.6.zip")
      when(mockVersionRepo.findByCandidateAndVersion(candidate, newVersion)).thenReturn(versionObj)

      val persistedObj = candidateObj.copy(id = new ObjectId("5423333bba78831a730c18e2"), default = newVersion)
      when(mockCandidateUpdateRepo.updateDefault(argThat[Candidate](samePropertyValuesAs(candidateObj)))).thenReturn(persistedObj)

      //when
      val response: ResponseEntity[SuccessResponse] = default(request, token, consumer, mockBinding)

      //then
      response.getStatusCode shouldBe ACCEPTED
      response.getBody.getId shouldBe "5423333bba78831a730c18e2"
      response.getBody.getMessage shouldBe "default groovy version: 2.3.6"

      verify(mockVersionRepo).findByCandidateAndVersion(candidate, newVersion)
      verify(mockCandidateUpdateRepo).updateDefault(argThat[Candidate](samePropertyValuesAs(persistedObj)))
    }

    "reject an invalid candidate version as default declaring bad request" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val version = "9.9.9"
      val request = new DefaultVersionRequest(candidate, version)

      when(candidateRepo.findByCandidate(candidate)).thenReturn(Candidate(candidate, "2.3.6"))
      when(versionRepo.findByCandidateAndVersion(candidate, version)).thenReturn(null)

      //when
      val e = intercept[VersionNotFoundException] {
        default(request, token, consumer, mockBinding)
      }

      //then
      e.getMessage shouldBe "invalid candidate version: groovy 9.9.9"
      verify(versionRepo).findByCandidateAndVersion(candidate, version)
    }

    "reject invalid candidate as bad request when marking default" in new ControllerUnderTest {
      //given
      val candidate = "groovee"
      val consumer = "groovee"
      val version = "2.3.7"
      val request = new DefaultVersionRequest(candidate, version)

      when(candidateRepo.findByCandidate(candidate)).thenReturn(null)

      //when
      val e = intercept[CandidateNotFoundException] {
        default(request, token, consumer, mockBinding)
      }

      //then
      e.getMessage shouldBe "not a valid candidate: groovee"
    }

    "fail validation if field is null" in new ControllerUnderTest {
      val version = "2.3.7"
      val consumer = null
      val request = new DefaultVersionRequest(null, version)

      val error = new ObjectError("defaultVersionRequest", "can not be null")
      when(mockBinding.hasErrors).thenReturn(true)
      when(mockBinding.getAllErrors).thenReturn(List[ObjectError](error))

      val e = intercept[ValidationException] {
        default(request, token, consumer, mockBinding)
      }

      e.getMessage should include("Error in object 'defaultVersionRequest'")
      e.getMessage should include("default message [can not be null]")
    }

    "deny access if invalid access token is provided" in new ControllerUnderTest {
      val candidate = "groovy"
      val version = "2.3.6"

      val request = new DefaultVersionRequest(candidate, version)

      val token = "invalid"

      val e = intercept[AuthorisationDeniedException] {
        default(request, token, consumer, mockBinding)
      }

      e.getMessage should include("Access prohibited.")
    }

    "deny access if invalid consumer header is provided" in new ControllerUnderTest {
      val candidate = "groovy"
      val version = "2.3.6"

      val request = new DefaultVersionRequest(candidate, version)

      val consumer = "invalid"

      val e = intercept[AuthorisationDeniedException] {
        default(request, token, consumer, mockBinding)
      }

      e.getMessage should include("Access prohibited.")
    }

    "allow access if admin consumer header is provided" in new ControllerUnderTest {
      //given
      val candidate = "groovy"
      val oldVersion = "2.3.5"
      val newVersion = "2.3.6"

      val request = new DefaultVersionRequest(candidate, newVersion)

      val candidateObj = Candidate(candidate, oldVersion)
      when(candidateRepo.findByCandidate(candidate)).thenReturn(candidateObj)

      val versionObj = Version(
        id = new ObjectId("5426b99bba78e60054fe48ca"), candidate, newVersion,
        url = "http://dl.bintray.com/groovy/maven/groovy-binary-2.3.6.zip")
      when(mockVersionRepo.findByCandidateAndVersion(candidate, newVersion)).thenReturn(versionObj)

      val persistedObj = candidateObj.copy(id = new ObjectId("5423333bba78831a730c18e2"), default = newVersion)
      when(mockCandidateUpdateRepo.updateDefault(argThat[Candidate](samePropertyValuesAs(candidateObj)))).thenReturn(persistedObj)

      val adminConsumer = "admin"

      //when
      val response: ResponseEntity[SuccessResponse] = default(request, token, adminConsumer, mockBinding)

      //then
      response.getStatusCode shouldBe ACCEPTED
    }
  }

  sealed trait ControllerUnderTest extends DefaultVersionController {
    val candidateUpdateRepo = mockCandidateUpdateRepo
    val versionRepo = mockVersionRepo
    val candidateRepo = mockCandidateRepo
    val secureHeaders = SecureHeaders("1HvHCVcDjUIjJxms8tGTkTdPSngIXqVtWKLluBl9qZ9TtM5AKI", "admin")
  }

}
