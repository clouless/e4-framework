package de.scandio.e4.testpackages.pqconf.actions

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.testpackages.pqconf.util.PocketQuerySeleniumHelper
import de.scandio.e4.worker.interfaces.Action
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.WebClient
import java.util.*

class PocketQuerySetupAction : Action() {

    private var start: Long = 0
    private var end: Long = 0

    override fun execute(webClient: WebClient, restClient: RestClient) {
        this.start = Date().time
        val webConfluence = webClient as WebConfluence
        val pqHelper = PocketQuerySeleniumHelper(webConfluence, webConfluence.dom)
        webConfluence.login()
        webConfluence.authenticateAdmin()
        pqHelper.createRestCustomDatasource("Wikipedia", "https://en.wikipedia.org/w/api.php")
        this.end = Date().time
    }

    override fun getTimeTaken(): Long {
        return end - start
    }


}