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
package io.sdkman.release.conf

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.MongoCredential.createMongoCRCredential
import com.mongodb.{Mongo, ServerAddress}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

@Configuration
class MongoConfig extends AbstractMongoConfiguration {

  @Value("#{systemEnvironment['MONGO_HOST']}")
  var mongoHost = "localhost"

  @Value("#{systemEnvironment['MONGO_PORT']}")
  var mongoPort = "27017"

  @Value("#{systemEnvironment['MONGO_DB_NAME']}")
  var mongoDbName = "sdkman"

  @Value("#{systemEnvironment['MONGO_USERNAME']}")
  var mongoUsername = ""

  @Value("#{systemEnvironment['MONGO_PASSWORD']}")
  var mongoPassword = ""

  override def getDatabaseName = mongoDbName

  def serverAddress(host: String, port: String) = new ServerAddress(host, port.toInt)

  def credentials(dbName: String, username: String, password: String) =
    List(createMongoCRCredential(username, dbName, password.toCharArray))

  override def mongo: Mongo = selectMongo.underlying

  def selectMongo = mongoHost match {
    case "localhost" =>
      MongoClient(
        serverAddress(mongoHost, mongoPort))
    case _ =>
      MongoClient(
        serverAddress(mongoHost, mongoPort),
        credentials(mongoDbName, mongoUsername, mongoPassword))
  }
}