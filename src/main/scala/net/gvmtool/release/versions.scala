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
package net.gvmtool.release

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

trait VersionPersistence {
  implicit val versionRepo: VersionRepo
  def save(v: Version) = versionRepo.save(v)
}

@Repository
trait VersionRepo extends MongoRepository[Version, ObjectId] {
  def findByCandidateAndVersion(candidate: String, version: String): Version
}

@Document(collection = "versions")
@TypeAlias("Version")
case class Version(id: ObjectId, candidate: String, version: String, url: String)

object Version {
  def apply(candidate: String, version: String, url: String): Version = Version(null, candidate, version, url)
}

class VersionNotFoundException(message: String) extends RuntimeException(message: String)

object VersionNotFoundException {
  def apply(candidate: String, version: String) =
    new VersionNotFoundException(s"invalid candidate version: $candidate $version")
}
