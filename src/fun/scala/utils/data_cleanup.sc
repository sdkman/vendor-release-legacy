import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoCollection}
import com.mongodb.casbah.Imports._

val userName = "gvm_dev"
val password = "ved_mvg"
val database = "gvm_dev"
val server = new ServerAddress("dharma.mongohq.com", 10063)
val credentials = MongoCredential.createMongoCRCredential(userName, database, password.toCharArray)
val client = MongoClient(server, List(credentials))
def db = client("gvm_dev")
def candidatesColl = db("candidates")

//gather candidate names
val candidates = candidatesColl.map(c => (c.getAs[String]("candidate"), c.getAs[String]("default")))

//drop it!
candidatesColl.drop()


def persistCandidate(c: String, d: String) =
  candidatesColl.save(MongoDBObject("_class" -> "Candidate", "candidate" -> c, "default" -> d))

//regenerate candidates collection
for{
  (candidateO, defaultO) <- candidates
  c <- candidateO
  d <- defaultO
} yield persistCandidate(c, d)
