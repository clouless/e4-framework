package de.scandio.e4.worker.util;

import de.scandio.e4.clients.WebConfluence;
import de.scandio.e4.clients.WebJira;
import de.scandio.e4.worker.client.ClientPlatform;
import de.scandio.e4.worker.rest.RestConfluence;
import de.scandio.e4.worker.interfaces.RestClient;
import de.scandio.e4.worker.interfaces.WebClient;
import de.scandio.e4.worker.rest.RestJira;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkerUtils {

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
