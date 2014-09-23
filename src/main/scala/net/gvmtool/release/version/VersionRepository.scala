package net.gvmtool.release.version

import net.gvmtool.release.request.VersionRequest
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
trait VersionRepository extends MongoRepository[Version, ObjectId]

@Document(collection = "versions")
case class Version(id: ObjectId, candidate: String, version: String, url: String)

object Version {
  def apply(r: VersionRequest): Version = Version(null, r.getCandidate, r.getVersion, r.getUrl)
}
