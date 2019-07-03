package de.scandio.e4.worker.util;

import de.scandio.e4.confluence.web.WebConfluence;
import de.scandio.e4.worker.confluence.rest.RestConfluence;
import de.scandio.e4.worker.interfaces.RestClient;
import de.scandio.e4.worker.interfaces.WebClient;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WorkerUtils {

	public static WebDriver newChromeDriver() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();
//		chromeOptions.addArguments("start-maximized");
		chromeOptions.addArguments("disable-extensions");
//		chromeOptions.addArguments("disable-automation");
		chromeOptions.addArguments("--start-maximized");
		chromeOptions.addArguments("--disable-extensions");
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--disable-impl-side-painting");
		chromeOptions.addArguments("--no-sandbox");
		//AGRESSIVE: options.setPageLoadStrategy(PageLoadStrategy.NONE); // https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
		chromeOptions.addArguments("--disable-infobars"); //https://stackoverflow.com/a/43840128/1689770
		chromeOptions.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
		chromeOptions.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
		chromeOptions.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc

		return new ChromeDriver(chromeOptions);
	}

	public static WebClient newChromeWebClient(String targetUrl, String inputDir, String outputDir, String username, String password) throws Exception {
		WebDriver driver = newChromeDriver();
		driver.manage().window().setSize(new Dimension(2000, 1500));
		return new WebConfluence(driver, new URI(targetUrl), inputDir, outputDir, username, password);
	}

//	public static WebClient newPhantomJsWebClient(String targetUrl, String outputDir, String username, String password) throws Exception {
//		WebDriverManager.phantomjs().setup();
//		WebDriver driver = new PhantomJSDriver();
//		driver.manage().window().setSize(new Dimension(1400, 1000));
//		return new WebConfluence(driver, new URI(targetUrl), outputDir, username, password);
//	}

	public static RestClient newRestClient(String targetUrl, String username, String password) {
		RestClient restClient = new RestConfluence(targetUrl, username, password);
		return restClient;
	}

	public static String getRuntimeName() {
		Thread currentThread = Thread.currentThread();
		return currentThread.getName() + "-" + currentThread.getId();
	}

	public static <T> List<T> getRandomItems(List<T> items, int howMany) {
		List<T> ret = new ArrayList<>();
		int size = items.size();
		for (int i = 0; i < howMany; i++) {
			int rnd = new Random().nextInt(size);
			ret.add(items.get(rnd));
		}
		return ret;
	}

	public static <T> T getRandomItem(List<T> items) {
		return getRandomItems(items, 1).get(0);
	}
}
