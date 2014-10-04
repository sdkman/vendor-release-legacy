package net.gvmtool.release.validate

import javax.validation.ValidationException

import net.gvmtool.release.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult

object Validate {
  def apply(fun: => ResponseEntity[SuccessResponse])(implicit binding: BindingResult) =
    if (binding.hasErrors) throw new ValidationException(binding.getAllErrors.toString)
    else fun
}