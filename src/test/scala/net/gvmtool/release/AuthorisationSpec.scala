package net.gvmtool.release

import net.gvmtool.release.response._
import org.scalatest.{ShouldMatchers, WordSpec}
import org.springframework.http.{HttpStatus, ResponseEntity}

class AuthorisationSpec extends WordSpec with ShouldMatchers {

  "authorised" should {

    "should invoke the function when valid auth token is found" in new AuthContext {

      implicit val header = "value"

      val x = Authorised {
        new ResponseEntity[SuccessResponse](SuccessResponse("id", "message"), HttpStatus.OK)
      }

      x.getStatusCode shouldBe HttpStatus.OK
      x.getBody.getId shouldBe "id"
      x.getBody.getMessage shouldBe "message"
    }

    "should throw authorisation denied exception when incorrect auth token is found" in new AuthContext {
      implicit val headers = "invalid"

      val e = intercept[AuthorisationDeniedException] {
        Authorised {
          new ResponseEntity[SuccessResponse](SuccessResponse("id", "message"), HttpStatus.OK)
        }
      }

      e.getMessage shouldBe "Invalid access token provided."
    }
  }

  sealed class AuthContext extends Authorisation {
    override implicit val accessToken: AccessToken = new AccessToken("value")
  }

}
