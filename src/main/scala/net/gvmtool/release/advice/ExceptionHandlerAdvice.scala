package net.gvmtool.release.advice

import net.gvmtool.release.candidate.CandidateNotFoundException
import net.gvmtool.release.version.VersionNotFoundException
import net.gvmtool.status._
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

}