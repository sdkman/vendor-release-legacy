package steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.ShouldMatchers
import support.Http

import scala.collection.mutable.{Map => MMap}

class SecuritySteps extends ScalaDsl with EN with ShouldMatchers {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  val world = MMap[String, String]()
  val statusCodes = Map("Unauthorized" -> 401, "OK" -> 200)
  var responseStatus = 0

  And( """^the Client is not Authorised and Authenticated$""") { () =>
    world.put("token", "invalid_token")
  }

  And( """^the Client is Authorised and Authenticated$""") { () =>
    val str = Http.obtainToken("/oauth/token", "auth_username", "auth_password").asString
    val token = mapper.readValue[Map[String, String]](str)
      .get("access_token")
      .getOrElse("invalid_token")
    world.put("token", s"Bearer $token")
  }

  And( """^the "(.*)" endpoint is accessed$""") { (endpoint: String) =>
    responseStatus = Http.statusWithToken(
      endpoint, world.get("token").getOrElse("invalid_token"))
  }

  And( """^an "(.*)" status is returned$""") { (status: String) =>
    responseStatus shouldBe statusCodes(status)
  }

}