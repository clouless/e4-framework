package de.scandio.e4.testpackages.livelytheme.actions

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.worker.interfaces.Action
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.WebClient
import de.scandio.e4.worker.util.WorkerUtils
import org.openqa.selenium.Dimension
import java.util.*

/**
 * @author Felix Grund
 */
open class CreateCustomElementPages(
        val spaceKey: String,
        val startPageTitle: String
) : Action() {

    protected var start: Long = 0
    protected var end: Long = 0

    override fun execute(webClient: WebClient, restClient: RestClient) {
        val webConfluence = webClient as WebConfluence
        val dom = webConfluence.dom
        webConfluence.login()
        this.start = Date().time
        webConfluence.goToPage(spaceKey, startPageTitle)

        this.end = Date().time
    }

    override fun getTimeTaken(): Long {
        return this.end - this.start
    }

}