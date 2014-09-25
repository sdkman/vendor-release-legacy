package net.gvmtool.release.releases

import net.gvmtool.release.request.VersionRequest
import net.gvmtool.release.response.SuccessResponse
import net.gvmtool.release.version.{Version, VersionRepository}
import org.bson.types.ObjectId
import org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs
import org.junit.runner.RunWith
import org.mockito.Matchers.argThat
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ShouldMatchers, WordSpec}
import org.springframework.http.{HttpStatus, ResponseEntity}

@RunWith(classOf[JUnitRunner])
class VersionControllerSpec extends WordSpec with ShouldMatchers with MockitoSugar {

  val repository = mock[VersionRepository]

  "version controller" should {
    "create a new candidate version" in new ControllerUnderTest {
      //arrange
      val candidate = "groovy"
      val version = "2.3.6"
      val url = "http://somehost/groovy-binary-2.3.6.zip"
      val request = new VersionRequest(candidate, version, url)
      val versionObj = Version(null, candidate, version, url)

      val persisted = versionObj.copy(id = new ObjectId("54205c4019b02458bdd828db"))
      when(repository.save(argThat[Version](samePropertyValuesAs(versionObj)))).thenReturn(persisted)

      //act
      val response: ResponseEntity[SuccessResponse] = publish(request)

      //assert
      response.getStatusCode shouldBe HttpStatus.CREATED
      response.getBody.getId shouldBe "54205c4019b02458bdd828db"
      verify(repository).save(versionObj)
    }
  }

  sealed trait ControllerUnderTest extends ReleaseController {
    val repo = repository
  }
}