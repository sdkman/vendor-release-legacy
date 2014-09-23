package steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.mongodb.casbah.MongoCollection
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.ShouldMatchers
import support.{Http, Mongo}

import scalaj.http.Http.Request

class ReleaseSteps extends ScalaDsl with EN with ShouldMatchers {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  val mongoDb = Mongo.primeDatabase("gvm")
  var candidateColl: MongoCollection = null
  var versionColl: MongoCollection = null

  var token = "invalid"

  var request: Request = null
  var response = Map[String, String]()

  Before() { s =>
    candidateColl = Mongo.createCollection(mongoDb, "candidates")
    versionColl = Mongo.createCollection(mongoDb, "versions")
  }

  After() { s =>
    Mongo.dropCollection(candidateColl)
    Mongo.dropCollection(versionColl)
  }

  Given( """^endpoint "(.*?)" exchanges credentials "(.*?)" and "(.*?)" for a bearer token$""") { (endpoint: String, username: String, password: String) =>
    val str = Http.obtainToken(endpoint, username, password).asString
    val bt = mapper.readValue[Map[String, String]](str).getOrElse("access_token", "invalid_token")
    token = s"Bearer $bt"
  }

  When( """^a JSON POST on the "(.*)" endpoint:$""") { (endpoint: String, json: String) =>
    request = Http.postJson(endpoint, json.stripMargin, token)
  }

  Then( """^the status received is (.*?)$""") { (responseCode: Int) =>
    request.responseCode shouldBe responseCode
  }

  Then( """^a valid identifier is received in the response$""") { () =>
    val Pattern = "([0-9a-fA-F]{24})".r
    mapper.readValue[Map[String, String]](request.asString).get("id") match {
      case Some(Pattern(id)) => println(s"Valid identifier found: $id")
      case Some(id) => fail(s"The id $id is not a valid ObjectID")
      case None => fail("No id found.")
    }
  }

  Then( """^"(.*?)" Version "(.*?)" with URL "(.*?)" was published$""") { (candidate: String, version: String, url: String) =>
    Mongo.versionPublished(versionColl, candidate, version, url) shouldBe true
  }

  Given( """^a "(.*?)" Version "(.*?)" with URL "(.*?)" already exists$""") { (candidate: String, version: String, url: String) =>
    Mongo.saveVersion(versionColl, candidate, version, url)
  }

  Given( """^the existing Default "(.*?)" Version is "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.saveCandidate(candidateColl, candidate, version)
  }

  When( """^the Default is set by a PUT on endpoint "(.*?)"$""") { (endpoint: String) =>
    request = Http.default(endpoint, token)
  }

  Then( """^the message "(.*?)" is received$""") { (message: String) =>
    mapper.readValue[Map[String, String]](request.asString).get("message") match {
      case Some(m) => m shouldBe message
      case None => fail("no message found")
    }
  }

  Then( """^the Default "(.*?)" Version has changed to "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.isDefault(candidateColl, candidate, version) shouldBe true
  }

  Given( """^Candidate "(.*?)" Version "(.*?)" does not exists$""") { (candidate: String, version: String) =>
    Mongo.versionExists(versionColl, candidate, version)
  }

  Given( """^Candidate "(.*?)" does not exist$""") { (candidate: String) =>
    Mongo.candidateExists(candidateColl, candidate)
  }
}
