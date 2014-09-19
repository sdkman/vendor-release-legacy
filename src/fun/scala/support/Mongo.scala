package support

import com.mongodb.casbah.Imports._

object Mongo {

  lazy val mongoClient = MongoClient()

  def primeDatabase(name: String): MongoDB = mongoClient(name)

  def createCollection(db: MongoDB, name: String): MongoCollection = db(name)

  def dropCollection(coll: MongoCollection) = coll.drop()

  def saveNonDefault(coll: MongoCollection, candidate: String, version: String, url: String) = {
    val candidateDBO =
      MongoDBObject("_id" -> "1234", "candidate" -> candidate, "default" -> "other",
        "versions" -> MongoDBList(
          MongoDBObject("version" -> version, "url" -> url)))
    coll.save(candidateDBO)
  }

  def hasCandidateVersion(coll: MongoCollection, candidate: String, version: String): Boolean = {
    coll.findOne(query(candidate), filter).exists { t =>
      val matches = for {
        versionDBO <- t.expand[List[BasicDBObject]]("versions").get
        v <- versionDBO.getAs[String]("version")
      } yield (v == version)
      matches.contains(true)
    }
  }

  def isDefault(coll: MongoCollection, candidate: String, version: String): Boolean =
    coll.findOne(query(candidate)).exists { t => t.get("default") == version}

  private val filter = MongoDBObject("versions" -> 1, "_id" -> false)

  private def query(candidate: String) = MongoDBObject("candidate" -> candidate)

}
