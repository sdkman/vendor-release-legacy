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
package steps

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.ShouldMatchers
import steps.World._
import support.Http

import scalaj.http.HttpException

class SecuritySteps extends ScalaDsl with EN with ShouldMatchers {

  And( """^the Client is not Authorised and Authenticated$""") { () =>
    token = "invalid_token"
    consumer = "invalid_consumer"
  }

  And( """^the Client is Authorised and Authenticated$""") { () =>
    token = "default_token"
  }

  And( """^the "(.*)" endpoint is accessed by "(.*)"$""") { (endpoint: String, consumer: String) =>
    request = Http.get(endpoint, token, consumer)

    //nasty scalaj hack prevents multiple posts
    import scalaj.http.Http.readString
    try {
      val (rc, hm, rs) = request.asHeadersAndParse[String](readString)
      responseCode = rc
      resultString = rs
    } catch {
      case e: HttpException => {
        responseCode = e.code
        resultString = e.body
      }
    }

  }
}