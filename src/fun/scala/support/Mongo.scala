package support

import com.mongodb.casbah.Imports._

object Mongo {

  lazy val mongoClient = MongoClient()

  def primeDatabase(name: String): MongoDB = mongoClient(name)

  def createCollection(db: MongoDB, name: String): MongoCollection = db(name)

  def dropCollection(coll: MongoCollection) = coll.drop()

  def versionPublished(coll: MongoCollection, candidate: String, version: String, url: String): Boolean =
    coll.findOne(MongoDBObject("candidate" -> candidate, "version" -> version, "url" -> url)).isDefined

  def saveVersion(coll: MongoCollection, candidate: String, version: String, url: String) =
    coll.save(MongoDBObject("candidate" -> candidate, "version" -> version, "url" -> url))

  def saveCandidate(coll: MongoCollection, candidate: String, default: String) =
    coll.save(MongoDBObject("candidate" -> candidate, "default" -> default))

  def isDefault(coll: MongoCollection, candidate: String, version: String): Boolean =
    coll.findOne(MongoDBObject("candidate" -> candidate, "default" -> version)).isDefined

  def versionExists(coll: MongoCollection, candidate: String, version: String): Boolean =
    coll.findOne(MongoDBObject("candidate" -> candidate, "version" -> version)).isDefined

  def candidateExists(coll: MongoCollection, candidate: String): Boolean =
    coll.findOne(MongoDBObject("candidate" -> candidate)).isDefined

}