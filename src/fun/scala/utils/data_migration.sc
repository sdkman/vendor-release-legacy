import com.mongodb.BasicDBObject
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoCollection}

val userName = "gvm_dev"
val password = "ved_mvg"
val database = "gvm_dev"
val server = new ServerAddress("dharma.mongohq.com", 10063)
val credentials = MongoCredential.createMongoCRCredential(userName, database, password.toCharArray)
val client = MongoClient(server, List(credentials))
def db = client("gvm_dev")
def candidatesColl = db("candidates")
def versionsColl = db("versions")
val versionsFilter = MongoDBObject("versions" -> 1, "_id" -> false)
def chosenCandidate(candidate: String) = MongoDBObject("candidate" -> candidate)
def fetchCandidateVersions(coll: MongoCollection, candidate: String): List[(String, String)] = {
  coll.findOne(chosenCandidate(candidate), versionsFilter).map { t =>
    for {
      versionDBO <- t.expand[List[BasicDBObject]]("versions").get
      v <- versionDBO.getAs[String]("version")
      u <- versionDBO.getAs[String]("url")
    } yield (v, u)
  }.getOrElse(throw new RuntimeException)
}
def allCandidates(coll: MongoCollection): Iterator[String] = coll.find().flatMap(_.getAs[String]("candidate"))

def doit = allCandidates(candidatesColl).foreach { candidate =>
  fetchCandidateVersions(candidatesColl, candidate).sorted.foreach { vobj =>
    versionsColl.save(
      MongoDBObject(
        "_class" -> "Version",
        "candidate" -> candidate,
        "version" -> vobj._1,
        "url" -> vobj._2)
    )
  }
}

doit