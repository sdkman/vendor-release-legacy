import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  format=Array("pretty", "html:build/reports/cucumber"),
  strict = true,
  features = Array("features"),
  glue = Array("steps", "support"),
  tags = Array("~@manual", "~@pending")
)
class RunCukes
