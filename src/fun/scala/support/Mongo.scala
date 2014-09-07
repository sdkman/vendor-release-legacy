package support

import com.mongodb.casbah.Imports._

object Mongo {

  lazy val mongoClient = MongoClient()

  def primeDatabase(name: String): MongoDB = mongoClient(name)

  def createCollection(db: MongoDB, name: String): MongoCollection = db(name)

  def dropCollection(coll: MongoCollection) = coll.drop()

}
