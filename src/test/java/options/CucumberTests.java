package options;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty"},
		glue = {"stepdefs"},
		features = {"src/test/features/MockGet.feature"})
public class CucumberTests {}
