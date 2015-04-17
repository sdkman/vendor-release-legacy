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

import net.gvmtool.release.request.DefaultVersionRequest
import net.gvmtool.release.response._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}
import org.springframework.http.HttpStatus._
import org.springframework.http.{HttpStatus, ResponseEntity}

@RunWith(classOf[JUnitRunner])
class AuthorisationSpec extends WordSpec with ShouldMatchers {

  "authorised" should {

    "should invoke the function when valid auth token and consumer header is found" in new AuthContext {
      val token = "value"
      val consumer = "groovy"
      val request = new DefaultVersionRequest("groovy", "2.4.1")

      val x = Authorised(token, consumer, request) {
        new ResponseEntity[SuccessResponse](SuccessResponse(OK.value, "id", "message"), OK)
      }

      x.getStatusCode shouldBe OK
      x.getBody.getId shouldBe "id"
      x.getBody.getMessage shouldBe "message"
    }

    "should invoke the function when valid auth token and admin header is found" in new AuthContext {
      val token = "value"
      val consumer = "admin"
      val request = new DefaultVersionRequest("groovy", "2.4.1")

      val x = Authorised(token, consumer, request) {
        new ResponseEntity[SuccessResponse](SuccessResponse(OK.value, "id", "message"), OK)
      }

      x.getStatusCode shouldBe OK
      x.getBody.getId shouldBe "id"
      x.getBody.getMessage shouldBe "message"
    }

    "should throw authorisation denied exception when incorrect auth token is found" in new AuthContext {
      val token = "invalid"
      val consumer = "groovy"
      val request = new DefaultVersionRequest("groovy", "2.4.1")

      val e = intercept[AuthorisationDeniedException] {
        Authorised(token, consumer, request) {
          new ResponseEntity[SuccessResponse](SuccessResponse(OK.value, "id", "message"), OK)
        }
      }

      e.getMessage shouldBe "Access prohibited."
    }

    "should throw authorisation denied exception when incorrect consumer header is found" in new AuthContext {
      val token = "value"
      val consumer = "groovy"
      val request = new DefaultVersionRequest("invalid", "2.4.1")

      val e = intercept[AuthorisationDeniedException] {
        Authorised(token, consumer, request) {
          new ResponseEntity[SuccessResponse](SuccessResponse(OK.value, "id", "message"), OK)
        }
      }

      e.getMessage shouldBe "Access prohibited."
    }
  }

  sealed class AuthContext extends Authorisation {
    override implicit val secureHeaders: SecureHeaders = new SecureHeaders("value", "admin")
  }

}
