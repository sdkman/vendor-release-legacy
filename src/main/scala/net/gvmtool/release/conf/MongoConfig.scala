package net.gvmtool.release.conf

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
  var mongoDbName = "gvm"

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