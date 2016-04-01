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

import com.mongodb.{Mongo, MongoClient, MongoCredential, ServerAddress}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.core.SimpleMongoDbFactory

import scala.collection.JavaConverters._

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

  override def mongo: Mongo = selectMongo

  override def mongoDbFactory: MongoDbFactory = new SimpleMongoDbFactory(mongo, mongoDbName)

  private def serverAddress(host: String, port: String) = new ServerAddress(host, port.toInt)

  private def selectMongo = mongoHost match {
    case "localhost" =>
      new MongoClient(
        serverAddress(mongoHost, mongoPort))
    case _ =>
      new MongoClient(
        serverAddress(mongoHost, mongoPort),
        credentials(mongoDbName, mongoUsername, mongoPassword))
  }

  private def credentials(dbName: String, username: String, password: String) =
    List(MongoCredential.createScramSha1Credential(username, dbName, password.toCharArray)).asJava
}