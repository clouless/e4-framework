package de.scandio.e4

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import de.scandio.e4.helpers.DomHelper
import de.scandio.e4.clients.WebConfluence
import de.scandio.e4.clients.WebJira
import de.scandio.e4.testpackages.pocketquery.PocketQueryConfTestPackage
import de.scandio.e4.worker.client.ApplicationName
import de.scandio.e4.worker.rest.RestConfluence
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.TestPackage
import de.scandio.e4.worker.interfaces.WebClient
import de.scandio.e4.worker.rest.RestJira
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

    protected var webClient: WebClient? = null
    protected var restClient: RestClient? = null

    protected var driver: WebDriver
    protected var util: Util
    protected var dom: DomHelper

    protected var screenshotCount = 0
    protected var dumpCount = 0

    init {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")

        this.driver = ChromeDriver(chromeOptions)
        this.driver.manage().window().size = Dimension(2000, 1500)
        this.util = Util()
        this.dom = DomHelper(driver, 40, 40)
        this.dom.defaultDuration = 40
        this.dom.defaultWaitTillPresent = 40
        this.dom.outDir = E4.OUT_DIR
        this.dom.screenshotBeforeClick = true
        this.dom.screenshotBeforeInsert = true

        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.getLogger("org.apache").level = Level.WARN

        setNewClients()
    }

    open fun refreshWebClient(login: Boolean = false, authenticate: Boolean = false) {
        webClient().quit()

        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        this.driver = ChromeDriver(chromeOptions)
        this.driver.manage().window().setSize(Dimension(1680, 1050))
        this.util = Util()
        this.dom = DomHelper(driver, 60, 60)
        this.dom.defaultDuration = 120
        this.dom.defaultWaitTillPresent = 120
        this.dom.outDir = E4.OUT_DIR
        this.dom.screenshotBeforeClick = true
        this.dom.screenshotBeforeInsert = true

        setNewClients()

        if (login) {
            webClient().login()
        }

        if (authenticate) {
            webClient().authenticateAdmin()
        }
    }

    fun setNewClients() {
        if (E4.APPLICATION_NAME == ApplicationName.confluence) {
            this.webClient = WebConfluence(driver, URI(E4.APPLICATION_BASE_URL), E4.IN_DIR, E4.OUT_DIR, E4.ADMIN_USERNAME, E4.ADMIN_PASSWORD)
            this.restClient = RestConfluence(E4.ADMIN_USERNAME, E4.ADMIN_PASSWORD)
        } else {
            this.webClient = WebJira(driver, URI(E4.APPLICATION_BASE_URL), E4.IN_DIR, E4.OUT_DIR, E4.ADMIN_USERNAME, E4.ADMIN_PASSWORD)
            this.restClient = RestJira(E4.ADMIN_USERNAME, E4.ADMIN_PASSWORD)
        }
    }

    open fun shot() {
        this.screenshotCount += 1
        val path = webClient().takeScreenshot("$screenshotCount-selenium-test.png")
        println(path)
    }

    open fun dump() {
        this.dumpCount += 1
        val path = webClient().dumpHtml("$dumpCount-selenium-test.html")
        println(path)
    }

    fun webClient() : WebClient {
        return this.webClient!!
    }

    fun restClient() : RestClient {
        return this.restClient!!
    }

    fun runPrepareActions(testPackage: TestPackage) {
        log.info("Running PREPARE actions")
        for (action in testPackage.setupActions) {
            try {
                log.info("Executing PREPARE action ${action.javaClass.simpleName}")
                action.execute(webClient(), restClient())
            } catch (e: Exception) {
                log.error("ERROR executing prepare action", e)
                dump()
                shot()
            }
        }
    }

}