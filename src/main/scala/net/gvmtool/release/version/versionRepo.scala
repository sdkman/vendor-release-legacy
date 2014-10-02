package net.gvmtool.release.version

import net.gvmtool.release.request.{DefaultVersionRequest, ReleaseRequest}
import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
trait VersionRepo extends MongoRepository[Version, ObjectId] {
  def findByCandidateAndVersion(candidate: String, version: String): Version = ???
}

@Document(collection = "versions")
@TypeAlias("Version")
case class Version(id: ObjectId, candidate: String, version: String, url: String)

object Version {
  def apply(r: ReleaseRequest): Version = Version(null, r.getCandidate, r.getVersion, r.getUrl)
}
