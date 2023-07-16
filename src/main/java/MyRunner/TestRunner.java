package MyRunner;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;

import java.net.URL;


	@RunWith(Cucumber.class)
	@CucumberOptions(
			features = "C:\\Users\\Akansha\\eclipse-workspace\\CucumberSeleniumFramework\\src\\main\\java\\Features\\deals.feature", //the path of the feature files
			glue={"stepDefinitions"}, //the path of the step definition files
			format= {"pretty","html:test-outout", "json:json_output/cucumber.json", "junit:junit_xml/cucumber.xml"}, //to generate different types of reporting
			monochrome = true, //display the console output in a proper readable format
			strict = true, //it will check if any step is not defined in step definition file
			dryRun = false //to check the mapping is proper between feature file and step def file
			//tags = {"~@SmokeTest" , "~@RegressionTest", "~@End2End"}			
			)
	 
	public class TestRunner {

	    private TestNGCucumberRunner testNGCucumberRunner;

	    public static RemoteWebDriver connection;

	    @BeforeClass(alwaysRun = true)
	    public void setUpCucumber() {
	         testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	    }

	    @BeforeMethod(alwaysRun = true)
	    @Parameters({ "browser", "version", "platform" })
	    public void setUpClass(String browser, String version, String platform) throws Exception {

	            String username = System.getenv("LT_USERNAME") == null ? "YOUR LT_USERNAME" : System.getenv("LT_USERNAME");
	            String accesskey = System.getenv("LT_ACCESS_KEY") == null ? "YOUR LT_ACCESS_KEY" : System.getenv("LT_ACCESS_KEY");

	            DesiredCapabilities capability = new DesiredCapabilities();
	            capability.setCapability(CapabilityType.BROWSER_NAME, browser);
	            capability.setCapability(CapabilityType.VERSION,version);
	            capability.setCapability(CapabilityType.PLATFORM, platform);
	            capability.setCapability("build", "Your Build Name");
	            String gridURL = "https://" + username + ":" + accesskey + "@hub.lambdatest.com/wd/hub";
	            System.out.println(gridURL);
	            connection = new RemoteWebDriver(new URL(gridURL), capability);
	            System.out.println(capability);
	            System.out.println(connection);
	}

	    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
	    public void feature(CucumberFeatureWrapper cucumberFeature) {
	    	testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
	    }

	    @DataProvider
	    public Object[][] features() {
	        return testNGCucumberRunner.provideFeatures();
	    }

	    @AfterClass(alwaysRun = true)
	    public void tearDownClass() throws Exception {
	        testNGCucumberRunner.finish();
	    }
	}