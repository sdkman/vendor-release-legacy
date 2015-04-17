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

import net.gvmtool.release.request.SimpleRequest
import net.gvmtool.release.response._
import org.springframework.http.ResponseEntity

case class SecureHeaders(token: String, admin: String)

trait Authorisation {
  implicit val secureHeaders: SecureHeaders
}

case class AuthorisationDeniedException(message: String) extends RuntimeException(message: String)

object Authorised {
  def apply(token: String, consumer: String, request: SimpleRequest)(fun: => ResponseEntity[SuccessResponse])(implicit secureHeaders: SecureHeaders) =
    if(validAccessToken(secureHeaders, token) && validConsumer(consumer, request)) fun
    else throw AuthorisationDeniedException("Access prohibited.")

  private def validAccessToken(secureHeaders: SecureHeaders, token: String) = secureHeaders.token == token

  val adminConsumer = "admin"

  private def validConsumer(consumer: String, request: SimpleRequest)(implicit secureHeaders: SecureHeaders) =
    consumer == request.getCandidate || consumer == secureHeaders.admin
}