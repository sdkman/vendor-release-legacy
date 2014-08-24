package net.gvmtool.conf

import com.mongodb.MongoCredential.createMongoCRCredential
import com.mongodb.{MongoClient, ServerAddress}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

@Configuration
class MongoConfigurationLike extends AbstractMongoConfiguration {

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

  def credentials(username: String, dbName: String, password: String) =
    ListBuffer(createMongoCRCredential(username, dbName, password.toCharArray))

  override def mongo = mongoHost match {
    case "localhost" =>
      new MongoClient
    case _ =>
      new MongoClient(serverAddress(mongoHost, mongoPort),
        credentials(mongoUsername, mongoDbName, mongoPassword))
  }

}