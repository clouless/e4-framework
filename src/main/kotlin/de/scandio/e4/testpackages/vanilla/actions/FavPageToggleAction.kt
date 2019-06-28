package de.scandio.e4.testpackages.vanilla.actions

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.worker.confluence.rest.RestConfluence
import de.scandio.e4.worker.interfaces.Action
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.WebClient
import org.openqa.selenium.Dimension
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author Felix Grund
 */
open class FavPageToggleAction : Action() {

    protected var start: Long = 0
    protected var end: Long = 0

    override fun execute(webClient: WebClient, restClient: RestClient) {
        val webConfluence = webClient as WebConfluence
        val restConfluence = restClient as RestConfluence
        val dom = webConfluence.dom
        val randomPageId = restConfluence.randomContentId
        webConfluence.login()
        webConfluence.goToPage(randomPageId)
        this.start = Date().time
        dom.click("#page-favourite")
        dom.awaitMilliseconds(100)
        this.end = Date().time
    }

    override fun getTimeTaken(): Long {
        return this.end - this.start
    }

}