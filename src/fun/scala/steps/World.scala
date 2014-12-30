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
import com.mongodb.casbah.MongoCollection
import support.Mongo

import scalaj.http.Http.Request

object World {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  var token = "invalid_token"
  val statusCodes = Map(
    "OK" -> 200,
    "CREATED" -> 201,
    "ACCEPTED" -> 202,
    "BAD_REQUEST" -> 400,
    "UNAUTHORIZED" -> 401,
    "CONFLICT" -> 409)
  var request: Request = null
  var response = Map[String, String]()

  val mongoDb = Mongo.primeDatabase("gvm")
  var candidateColl: MongoCollection = null
  var versionColl: MongoCollection = null

}
