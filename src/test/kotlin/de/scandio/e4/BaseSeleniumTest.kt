package de.scandio.e4

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import de.scandio.e4.helpers.DomHelper
import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.worker.confluence.rest.RestConfluence
import de.scandio.e4.worker.util.Util
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.LoggerFactory
import java.net.URI

open abstract class BaseSeleniumTest {

    private val log = LoggerFactory.getLogger(javaClass)

//    protected var BASE_URL = "http://confluence-cluster-6153-lb:26153/"
    protected var BASE_URL = "http://e4-test:8090/"
    protected val OUT_DIR = "/tmp/e4/out"
    protected val IN_DIR = "/tmp/e4/in"
    protected val USERNAME = "admin"
    protected val PASSWORD = "admin"

    protected var driver: WebDriver
    protected var util: Util
    protected var dom: DomHelper
    protected var webConfluence: WebConfluence
    protected var restConfluence: RestConfluence

    protected var screenshotCount = 0
    protected var dumpCount = 0

    init {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        this.driver = ChromeDriver(chromeOptions)
        this.driver.manage().window().setSize(Dimension(2000, 1500))
        this.util = Util()
        this.dom = DomHelper(driver, 40, 40)
        this.dom.defaultDuration = 40
        this.dom.defaultWaitTillPresent = 40
        this.dom.outDir = OUT_DIR
        this.dom.screenshotBeforeClick = true
        this.dom.screenshotBeforeInsert = true
        this.webConfluence = WebConfluence(driver, URI(BASE_URL), IN_DIR, OUT_DIR, USERNAME, PASSWORD)
        this.restConfluence = RestConfluence(BASE_URL, USERNAME, PASSWORD)

        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.getLogger("org.apache").level = Level.WARN
    }

    open fun refreshWebClient(login: Boolean = false, authenticate: Boolean = false) {
        this.webConfluence.quit()

        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        this.driver = ChromeDriver(chromeOptions)
        this.driver.manage().window().setSize(Dimension(1680, 1050))
        this.util = Util()
        this.dom = DomHelper(driver, 60, 60)
        this.dom.defaultDuration = 120
        this.dom.defaultWaitTillPresent = 120
        this.dom.outDir = OUT_DIR
        this.dom.screenshotBeforeClick = true
        this.dom.screenshotBeforeInsert = true

        this.webConfluence = WebConfluence(driver, URI(BASE_URL), IN_DIR, OUT_DIR, USERNAME, PASSWORD)

        if (login) {
            webConfluence.login()
        }

        if (authenticate) {
            webConfluence.authenticateAdmin()
        }
    }

    open fun shot() {
        this.screenshotCount += 1
        val path = webConfluence.takeScreenshot("$screenshotCount-selenium-test.png")
        println(path)
    }

    open fun dump() {
        this.dumpCount += 1
        val path = webConfluence.dumpHtml("$dumpCount-selenium-test.html")
        println(path)
    }

}