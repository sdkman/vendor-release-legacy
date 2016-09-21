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
package support

import com.mongodb._
import com.mongodb.client.{MongoCollection, MongoDatabase}

import scala.language.implicitConversions

object Mongo {

  lazy val mongoClient = new MongoClient()

  def primeDatabase(name: String): MongoDatabase = mongoClient.getDatabase(name)

  def candidatesCollection(implicit db: MongoDatabase): MongoCollection[BasicDBObject] = getCollection(db, "candidates")

  def versionsCollection(implicit db: MongoDatabase): MongoCollection[BasicDBObject] = getCollection(db, "versions")

  def getCollection(db: MongoDatabase, name: String) = db.getCollection(name, classOf[BasicDBObject])

  def createCollection(db: MongoDatabase, name: String) = db.createCollection(name)

  def dropCollection(coll: MongoCollection[BasicDBObject]) = coll.drop()

  def versionPublished(coll: MongoCollection[BasicDBObject], candidate: String, version: String, url: String, platform: String): Boolean =
    coll.count(Map("candidate" -> candidate, "version" -> version, "url" -> url, "platform" -> platform)) > 0

  def saveVersion(coll: MongoCollection[BasicDBObject], candidate: String, version: String, url: String) =
    coll.insertOne(Map("candidate" -> candidate, "version" -> version, "url" -> url, "platform" -> "UNIVERSAL"))

  def saveCandidate(coll: MongoCollection[BasicDBObject], candidate: String, default: String) =
    coll.insertOne(Map("candidate" -> candidate, "default" -> default))

  def isDefault(coll: MongoCollection[BasicDBObject], candidate: String, version: String): Boolean =
    coll.count(Map("candidate" -> candidate, "default" -> version)) > 0

  def versionExists(coll: MongoCollection[BasicDBObject], candidate: String, version: String): Boolean =
    coll.count(Map("candidate" -> candidate, "version" -> version)) > 0

  def candidateExists(coll: MongoCollection[BasicDBObject], candidate: String): Boolean =
    coll.count(Map("candidate" -> candidate)) > 0


  implicit def basicDbObject(keyValues: Map[String, AnyRef]): BasicDBObject = {
    val bdo = new BasicDBObject()
    keyValues.foreach(kv => bdo.append(kv._1, kv._2))
    bdo
  }
}