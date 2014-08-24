package net.gvmtool

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@ComponentScan(Array("net.gvmtool"))
class Application

object Application {
  def main(args: Array[String]): Unit = SpringApplication.run(classOf[Application], args: _*)
}


