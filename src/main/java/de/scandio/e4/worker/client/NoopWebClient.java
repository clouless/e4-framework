package de.scandio.e4.worker.client;

import de.scandio.e4.worker.interfaces.WebClient;
import org.openqa.selenium.WebDriver;

public class NoopWebClient implements WebClient {
	@Override
	public WebDriver getWebDriver() {
		return null;
	}

	@Override
	public String takeScreenshot(String screenshotName) {
		return null;
	}

	@Override
	public String dumpHtml(String dumpName) {
		return null;
	}

	@Override
	public void quit() {

	}

	@Override
	public void login() {

	}

	@Override
	public String getNodeId() {
		return null;
	}

	@Override
	public String getUser() {
		return null;
	}

	@Override
	public void refreshDriver() {

	}
}
