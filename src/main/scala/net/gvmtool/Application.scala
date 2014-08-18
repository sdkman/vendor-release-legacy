package net.gvmtool

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

@EnableAutoConfiguration
class Application

object Application {
  def main(args: Array[String]): Unit = SpringApplication.run(classOf[Application], args :_ *)
}


