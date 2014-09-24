package net.gvmtool.release.conf

import com.mongodb.MongoCredential.createMongoCRCredential
import com.mongodb.{MongoClient, ServerAddress}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert._
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

@Configuration
class MongoConfig extends AbstractMongoConfiguration {

  @Value("#{systemEnvironment['MONGO_HOST']}")
  var mongoHost = "localhost"

  @Value("#{systemEnvironment['MONGO_PORT']}")
  var mongoPort = "27017"

  @Value("#{systemEnvironment['MONGO_DB_NAME']}")
  var mongoDbName = "gvm"

  @Value("#{systemEnvironment['MONGO_USERNAME']}")
  var mongoUsername = ""

  @Value("#{systemEnvironment['MONGO_PASSWORD']}")
  var mongoPassword = ""

  override def getDatabaseName = mongoDbName

  def serverAddress(host: String, port: String) = new ServerAddress(host, port.toInt)

  def credentials(dbName: String, username: String, password: String) =
    ListBuffer(createMongoCRCredential(username, dbName, password.toCharArray))

  override def mongo = mongoHost match {
    case "localhost" =>
      new MongoClient
    case _ =>
      new MongoClient(serverAddress(mongoHost, mongoPort),
        credentials(mongoDbName, mongoUsername, mongoPassword))
  }
}