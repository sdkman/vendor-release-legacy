package support

import scalaj.http.Http.Request
import scalaj.http.{Http => HttpClient}

object Http {

  val host = "http://localhost:8080"

  def obtainToken(endpoint: String = "/oauth/token", username: String = "auth_username", password: String = "auth_password"): Request = {
    HttpClient.postData(
      s"$host$endpoint",
      s"password=$password&username=$username&grant_type=password&scope=read%20write&client_secret=client_secret&client_id=client_id")
      .auth("client_id", "client_secret")
      .header("Content-Type", "application/x-www-form-urlencoded")
  }

  def statusWithToken(endpoint: String, token: String): Int = {
    HttpClient(s"$host$endpoint")
      .headers(
        "Authorization" -> token,
        "Accept" -> "application/json",
        "Content-Type" -> "application/json"
      ).responseCode
  }

  def release(endpoint: String, json: String, token: String): Request = {
    HttpClient.postData(s"$host/$endpoint", json)
      .headers(
        "Authorization" -> token,
        "Accept" -> "application/json",
        "Content-Type" -> "application/json"
      )
  }

  def default(endpoint: String, token: String): Request = {
    HttpClient(endpoint)
      .method("PUT")
      .headers(
        "Authorization" -> token,
        "Accept" -> "application/json",
        "Content-Type" -> "application/json"
      )
  }
}
