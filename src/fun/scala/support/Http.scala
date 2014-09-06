package support

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scalaj.http.{Http => HttpClient}

object Http {

  val host = "http://localhost:8080"

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  def obtainToken: String = {
    val str = HttpClient.postData(s"$host/oauth/token", "password=auth_password&username=auth_username&grant_type=password&scope=read%20write&client_secret=client_secret&client_id=client_id")
      .auth("client_id", "client_secret")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .asString

    mapper.readValue[Map[String, String]](str)
      .get("access_token")
      .getOrElse("invalid_token")
  }

  def statusWithToken(endpoint: String, token: String ) = {
    HttpClient(s"$host$endpoint")
      .headers(
        "Authorization" -> token,
        "Accept" -> "application/json",
        "Content-Type" -> "application/json"
      ).responseCode
  }
}
