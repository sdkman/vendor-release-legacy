package net.gvmtool.conf

import com.mongodb.{Mongo, MongoClient}
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

@Configuration
class MongoConfiguration extends AbstractMongoConfiguration {
  override def getDatabaseName: String = "gvm"
  override def mongo: Mongo = new MongoClient
}
