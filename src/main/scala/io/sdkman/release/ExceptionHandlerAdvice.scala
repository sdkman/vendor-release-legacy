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
package io.sdkman.release

import javax.validation.ValidationException

import org.springframework.dao.DataAccessException
import org.springframework.web.bind.annotation._

@ControllerAdvice
class ExceptionHandlerAdvice {

  @ExceptionHandler
  def handle(e: VersionNotFoundException) = BadRequest(e.getMessage)

  @ExceptionHandler
  def handle(e: CandidateNotFoundException) = BadRequest(e.getMessage)

  @ExceptionHandler
  def handle(e: DataAccessException) = ServiceUnavailable(e.getMessage)

  @ExceptionHandler
  def handle(e: ValidationException) = BadRequest(e.getMessage)

  @ExceptionHandler
  def handle(e: DuplicateVersionException) = Conflict(e.getMessage)

  @ExceptionHandler
  def handle(e: AuthorisationDeniedException) = Forbidden(e.getMessage)
}