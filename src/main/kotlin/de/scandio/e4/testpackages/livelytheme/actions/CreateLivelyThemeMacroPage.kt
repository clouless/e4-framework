package de.scandio.e4.testpackages.livelytheme.actions

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.worker.confluence.rest.RestConfluence
import de.scandio.e4.worker.interfaces.Action
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.WebClient
import org.openqa.selenium.Keys
import org.openqa.selenium.interactions.Actions
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author Felix Grund
 */
open class CreateLivelyThemeMacroPage (
        val spaceKey: String,
        val macros: Map<String, String>
    ) : Action() {

    private val log = LoggerFactory.getLogger(javaClass)

    protected var start: Long = 0
    protected var end: Long = 0

    var webConfluence: WebConfluence? = null

    override fun execute(webClient: WebClient, restClient: RestClient) {
        val webConfluence = webClient as WebConfluence
        this.webConfluence = webConfluence
        val dom = webConfluence.dom
        val numMacros = macros.keys.size
        val randIndex = Random().nextInt(numMacros)
        val macroId = "lively-column-width"//macros.keys.toTypedArray()[randIndex]
        val pageTitle = "Macro Page $macroId (${Date().time})"
        webConfluence.login()
        this.start = Date().time
        webConfluence.navigateTo("pages/createpage.action?spaceKey=$spaceKey")
        dom.awaitElementPresent("#wysiwyg")
        dom.click("#wysiwyg")
        webConfluence.setPageTitleInEditor(pageTitle)
        when(macroId) {
            "lively-button" -> {
                insertLivelyMacro(macroId, mapOf(
                        "text" to "My Button",
                        "link" to "Lively Theme Home"
                ))
            }
            "lively-list" -> {
                insertLivelyMacro(macroId)
            }
            "lively-menu" -> {
                insertLivelyMacro(macroId)
                dom.click("#rte-button-bullist")
                webConfluence.simulateBulletList(arrayOf("Test 1", "Test 2", "Test 3"))
            }
            "lively-widget" -> {
                insertLivelyMacro(macroId)
                webConfluence.simulateText("Test")
            }
            "lively-margin" -> {
                insertLivelyMacro(macroId, mapOf(
                        "margin" to "300px"
                ))
                webConfluence.simulateText("Test")
            }
            "lively-column-width" -> {
                webConfluence.focusEditor()
                webConfluence.setTwoColumnLayout()
                webConfluence.simulateText("COLUMN")
                insertLivelyMacro(macroId, mapOf("width" to "300px"))
            }
        }
        webConfluence.savePage()
        this.end = Date().time
    }

    fun insertLivelyMacro(macroId: String, macroParameters: Map<String, String> = emptyMap()) {
        webConfluence!!.insertMacro(macroId, macros[macroId]!!, macroParameters)
    }

    override fun getTimeTaken(): Long {
        return this.end - this.start
    }


}