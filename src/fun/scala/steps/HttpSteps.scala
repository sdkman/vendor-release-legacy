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
import org.scalatest._
import steps.World._
import support.Http

import scalaj.http.HttpException

class HttpSteps extends ScalaDsl with EN with ShouldMatchers {
  When( """^a JSON POST on the "(.*)" endpoint:$""") { (endpoint: String, json: String) =>
    request = Http.postJson(endpoint, json.stripMargin, token, consumer)

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

  When( """^a JSON PUT on the "(.*?)" endpoint:$""") { (endpoint: String, payload: String) =>
    request = Http.putJson(endpoint, payload.stripMargin, token, consumer)

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

  And( """^the "(.*)" endpoint is accessed$""") { (endpoint: String) =>
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
