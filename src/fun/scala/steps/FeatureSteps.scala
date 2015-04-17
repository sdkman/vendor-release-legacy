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

class FeatureSteps extends ScalaDsl with EN with ShouldMatchers {

  Then( """^the status received is "(.*)"$""") { (status: String) =>
    responseCode shouldBe statusCodes(status)
  }

  Then( """^a valid identifier is received in the response$""") { () =>
    val Pattern = "([0-9a-fA-F]{24})".r
    mapper.readValue[Map[String, String]](resultString).get("id") match {
      case Some(Pattern(id)) => println(s"Valid identifier found: $id")
      case Some(id) => fail(s"The id $id is not a valid ObjectID")
      case None => fail("No id found.")
    }
  }

  Then( """^the message "(.*?)" is received$""") { (message: String) =>
    extractMessage(resultString) shouldBe message
  }

  Then( """^the error message received includes "(.*?)"$""") { (message: String) =>
    extractMessage(resultString) should include(message)
  }

  def extractMessage(str: String) = mapper.readValue[Map[String, String]](str).getOrElse("message", "invalid")
}
