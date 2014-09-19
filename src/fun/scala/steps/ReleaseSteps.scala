package steps

import com.mongodb.casbah.MongoCollection
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.ShouldMatchers
import support.Http.obtainToken
import support.{Http, Mongo}

class ReleaseSteps extends ScalaDsl with EN with ShouldMatchers {

  val mongoDb = Mongo.primeDatabase("gvm")
  var mongoColl: MongoCollection = null

  var candidateState = "invalid"
  var versionState = "invalid"
  var urlState = "invalid"
  var response = Map[String,String]()

  Before("@release") { s =>
    mongoColl = Mongo.createCollection(mongoDb, "candidates")
  }

  After("@release") { s =>
    Mongo.dropCollection(mongoColl)
  }

  Given( """^a Candidate "(.*?)"$""") { (candidate: String) =>
    candidateState = candidate
  }

  Given( """^a new Version "(.*?)" with URL (.*?)$""") { (version: String, url: String) =>
    versionState = version
    urlState = url
  }

  Given( """^an older Version "(.*?)" with URL (.*?)$""") { (version: String, url: String) =>
    Mongo.saveNonDefault(mongoColl, candidateState, version, url)
  }

  When( """^the new Version is Released as (.*)$""") { (release: String) =>
    val default = (release == "Default")
    Http.release(candidateState, versionState, urlState, default, obtainToken()) shouldBe 200
  }

  Then( """^the "(.*?)" Version "(.*?)" was Released$""") { (candidate: String, version: String) =>
    Mongo.hasCandidateVersion(mongoColl, candidate, version) shouldBe true
  }

  Then( """^the Default Version for "(.*?)" is "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.isDefault(mongoColl, candidate, version) shouldBe true
  }
}
