package net.gvmtool.release.candidate

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria._
import org.springframework.data.mongodb.core.query.Query._
import org.springframework.data.mongodb.core.query.Update._
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
trait CandidateGeneralRepo extends MongoRepository[Candidate, ObjectId] {
  def findByCandidate(candidate: String): Candidate
}

@Repository
class CandidateUpdateRepo @Autowired()(val mongoTemplate: MongoTemplate)  {
  def updateDefault(candidate: Candidate): Candidate =
    mongoTemplate.findAndModify(
      query(where("candidate").is(candidate.candidate)),
      update("default", candidate.default),
      classOf[Candidate])
}

@Document(collection = "candidates")
@TypeAlias("Candidate")
case class Candidate(id: ObjectId, candidate: String, default: String)

object Candidate {
  def apply(candidate: String, default: String) = new Candidate(null, candidate, default)
}

class CandidateNotFoundException(message: String) extends RuntimeException(message: String)

object CandidateNotFoundException {
  def apply(candidate: String) = new CandidateNotFoundException(s"not a valid candidate: $candidate")
}