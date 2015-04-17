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
import support.Mongo

class PersistenceSteps extends ScalaDsl with EN with ShouldMatchers {
  Then( """^"(.*?)" Version "(.*?)" with URL "(.*?)" was published$""") { (candidate: String, version: String, url: String) =>
    Mongo.versionPublished(versionColl, candidate, version, url) shouldBe true
  }

  Given( """^a "(.*?)" Version "(.*?)" with URL "(.*?)" already exists$""") { (candidate: String, version: String, url: String) =>
    Mongo.saveVersion(versionColl, candidate, version, url)
  }

  Given( """^the existing Default "(.*?)" Version is "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.saveCandidate(candidateColl, candidate, version)
  }

  Then( """^the Default "(.*?)" Version has changed to "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.isDefault(candidateColl, candidate, version) shouldBe true
  }

  Given( """^Candidate "(.*?)" Version "(.*?)" does not exists$""") { (candidate: String, version: String) =>
    Mongo.versionExists(versionColl, candidate, version) shouldBe false
  }

  Given( """^Candidate "(.*?)" does not exist$""") { (candidate: String) =>
    Mongo.candidateExists(candidateColl, candidate) shouldBe false
  }

  Given( """^the appropriate candidate already exists$""") { () =>
    Mongo.saveCandidate(candidateColl, "groovy", "2.3.6")
  }

}
