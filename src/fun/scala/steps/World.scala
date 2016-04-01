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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import support.Mongo

import scalaj.http.Http.Request

object World {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  var token = "invalid_token"
  var consumer = "invalid_consumer"
  val statusCodes = Map(
    "OK" -> 200,
    "CREATED" -> 201,
    "ACCEPTED" -> 202,
    "BAD_REQUEST" -> 400,
    "FORBIDDEN" -> 403,
    "CONFLICT" -> 409)
  var request: Request = null
  var response = Map[String, String]()

  var responseCode: Int = 0
  var resultString: String = null

  implicit val mongoDb = Mongo.primeDatabase("sdkman")
  var candidateColl: MongoCollection[BasicDBObject] = null
  var versionColl: MongoCollection[BasicDBObject] = null

}
