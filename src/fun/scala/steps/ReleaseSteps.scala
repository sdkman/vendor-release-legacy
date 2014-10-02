package steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.mongodb.casbah.MongoCollection
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.ShouldMatchers
import support.{Http, Mongo}

import scalaj.http.Http.Request
import scalaj.http.HttpException

class ReleaseSteps extends ScalaDsl with EN with ShouldMatchers {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  val statusCodes = Map("OK" -> 200, "CREATED" -> 201, "ACCEPTED" -> 202, "BAD_REQUEST" -> 400, "UNAUTHORIZED" -> 401)

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

  Then( """^the status received is "(.*)"$""") { (status: String) =>
    request.responseCode shouldBe statusCodes(status)
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

  When( """^a JSON PUT on the "(.*?)" endpoint:$""") { (endpoint: String, payload: String) =>
    request = Http.putJson(endpoint, payload.stripMargin, token)
  }

  Then( """^the message "(.*?)" is received$""") { (message: String) =>
    try {
      extractMessage(request.asString) shouldBe message
    } catch {
      case e: HttpException => extractMessage(e.body) shouldBe message
    }
  }

  def extractMessage(str: String) = mapper.readValue[Map[String, String]](str).getOrElse("message", "invalid")

  Then( """^the Default "(.*?)" Version has changed to "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.isDefault(candidateColl, candidate, version) shouldBe true
  }

  Given( """^Candidate "(.*?)" Version "(.*?)" does not exists$""") { (candidate: String, version: String) =>
    Mongo.versionExists(versionColl, candidate, version) shouldBe false
  }

  Given( """^Candidate "(.*?)" does not exist$""") { (candidate: String) =>
    Mongo.candidateExists(candidateColl, candidate) shouldBe false
  }
}
