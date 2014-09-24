package steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.ShouldMatchers
import support.Http

import scalaj.http.Http.Request

class SecuritySteps extends ScalaDsl with EN with ShouldMatchers {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  var token = "invalid_token"
  val statusCodes = Map("Unauthorized" -> 401, "OK" -> 200, "Created" -> 201)
  var request: Request = null

  And( """^the Client is not Authorised and Authenticated$""") { () =>
    token = "invalid_token"
  }

  And( """^the Client is Authorised and Authenticated$""") { () =>
    val str = Http.obtainToken("/oauth/token", "auth_username", "auth_password").asString
    val bt = mapper.readValue[Map[String, String]](str).getOrElse("access_token", "invalid_token")
    token = s"Bearer $bt"
  }

  And( """^the "(.*)" endpoint is accessed$""") { (endpoint: String) =>
    request = Http.post(endpoint, token)
  }

  When( """^the "(.*?)" endpoint receives a POST with valid payload$""") { (endpoint: String) =>
    val payload =
      """
        |{
        |  "candidate" : "groovy",
        |  "version" : "2.3.6",
        |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
        |}
      """.stripMargin

    request = Http.postJson(endpoint, payload, token)
  }

  And( """^an "(.*)" status is returned$""") { (status: String) =>
    request.responseCode shouldBe statusCodes(status)
  }

}