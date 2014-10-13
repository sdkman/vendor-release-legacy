/**
 * Copyright 2014 Marco Vermeulen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
  val statusCodes = Map("UNAUTHORIZED" -> 401, "OK" -> 200, "CREATED" -> 201)
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

  When( """^the "(.*?)" endpoint receives a POST with valid payload:$""") { (endpoint: String, payload: String) =>
    request = Http.postJson(endpoint, payload.stripMargin, token)
  }

  And( """^an "(.*)" status is returned$""") { (status: String) =>
    request.responseCode shouldBe statusCodes(status)
  }

}