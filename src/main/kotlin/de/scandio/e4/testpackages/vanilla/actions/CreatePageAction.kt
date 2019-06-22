package de.scandio.e4.testpackages.vanilla.actions

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.worker.confluence.rest.RestConfluence
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.Action
import de.scandio.e4.worker.interfaces.WebClient
import java.util.*

class CreatePageAction(
        val spaceKey: String,
        var pageTitle: String,
        var pageContent: String = "",
        var parentPageTitle: String = "",
        var useRest: Boolean = false,
        var appendUsernameToPageTitle: Boolean = false,
        var appendTimestampToPageTitle: Boolean = false
) : Action() {

    private var start: Long = 0
    private var end: Long = 0

    override fun execute(webClient: WebClient, restClient: RestClient) {
        val webConfluence = webClient as WebConfluence
        val restConfluence = restClient as RestConfluence
        if (appendUsernameToPageTitle) {
            pageTitle += " (${webClient.username})"
        }

        if (appendTimestampToPageTitle) {
            pageTitle += " (${Date().time})"
        }

        if (!useRest) {
            webConfluence.login()
        }

        this.start = Date().time
        if (pageContent.isEmpty()) {
            webConfluence.createDefaultPage(spaceKey, pageTitle)
        } else if (useRest) {
            restConfluence.createPage(spaceKey, pageTitle, pageContent, parentPageTitle)
        } else {
            webConfluence.createCustomPage(spaceKey, pageTitle, pageContent)
        }

        this.end = Date().time
    }

    override fun getTimeTaken(): Long {
        return this.end - this.start
    }


}