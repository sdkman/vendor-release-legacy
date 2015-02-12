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

  def get(endpoint: String)(implicit token: String) = withOptions(HttpClient.get(s"$host$endpoint"))

  def post(endpoint: String)(implicit token: String) = withOptions(HttpClient.post(s"$host$endpoint"))

  def postJson(endpoint: String, json: String)(implicit token: String) = withOptions(HttpClient.postData(s"$host$endpoint", json))

  def putJson(endpoint: String, json: String)(implicit token: String) = withOptions(HttpClient.postData(s"$host$endpoint", json).method("PUT"))

  private def withOptions(http: Request)(implicit token: String): Request = http.headers(
    "access_token" -> token,
    "Accept" -> "application/json",
    "Content-Type" -> "application/json")
    .option(HttpOptions.connTimeout(1000))
    .option(HttpOptions.readTimeout(5000))

}
