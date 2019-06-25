package de.scandio.e4.testpackages.livelytheme.actions

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.worker.confluence.rest.RestConfluence
import de.scandio.e4.worker.interfaces.Action
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.WebClient
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

    override fun execute(webClient: WebClient, restClient: RestClient) {
        val webConfluence = webClient as WebConfluence
        val dom = webConfluence.dom
        val numMacros = macros.keys.size
        val randIndex = Random().nextInt(numMacros)
        val macroId = macros.keys.toTypedArray()[randIndex]
        val macroName = macros[macroId]!!
        val pageTitle = "Macro Page $macroId (${Date().time})"
        this.start = Date().time
        webConfluence.navigateTo("pages/createpage.action?spaceKey=$spaceKey")
        dom.awaitElementPresent("#wysiwyg")
        dom.click("#wysiwyg")
        webConfluence.setPageTitleInEditor(pageTitle)
        webConfluence.openMacroBrowser(macroId, macroName) //TODO
        macroFns[macroId]!!.invoke()
        this.end = Date().time
    }

    val macroFns = mapOf(
            "lively-dashboard-element" to fun() {

            },
            "lively-column-width" to fun() {},
            "lively-button" to fun() {},
            "lively-widget" to fun() {},
            "lively-menu" to fun() {},
            "lively-list" to fun() {},
            "lively-margin" to fun() {}
    )

    private fun createMacroPage(macroId: String) {


    }

    fun createMacroPage(spaceKey: String, pageTitle: String, macroId: String, macroName: String,
                        macroParameters: HashMap<String, String>) {
        // TODO
//        navigateTo("pages/createpage.action?spaceKey=$spaceKey")
//        dom.awaitElementPresent("#wysiwyg")
//        dom.click("#wysiwyg")
//        setPageTitleInEditor(pageTitle)
//        insertMacro(macroId, macroName, macroParameters)
//        savePage()
    }

    override fun getTimeTaken(): Long {
        return this.end - this.start
    }


}