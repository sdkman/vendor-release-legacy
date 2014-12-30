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
package support

import scalaj.http.Http.Request
import scalaj.http.{Http => HttpClient, HttpOptions}

object Http {

  val host = "http://localhost:8080"

  def obtainToken(endpoint: String, username: String, password: String): Request = {
    HttpClient.postData(
      s"$host$endpoint",
      s"password=$password&username=$username&grant_type=password&scope=read%20write&client_secret=client_secret&client_id=client_id")
      .auth("client_id", "client_secret")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(5000))
  }

  def post(endpoint: String, token: String): Request = {
    HttpClient(s"$host$endpoint")
      .headers(
        "Authorization" -> token,
        "Accept" -> "application/json",
        "Content-Type" -> "application/json")
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(5000))
  }

  def postJson(endpoint: String, json: String, token: String): Request = {
    HttpClient.postData(s"$host$endpoint", json)
      .headers(
        "Authorization" -> token,
        "Accept" -> "application/json",
        "Content-Type" -> "application/json"
      )
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(5000))
  }

  def putJson(endpoint: String, json: String, token: String): Request = {
    HttpClient.postData(s"$host$endpoint", json)
      .method("PUT")
      .headers(
        "Authorization" -> token,
        "Accept" -> "application/json",
        "Content-Type" -> "application/json"
      )
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(5000))
  }
}
