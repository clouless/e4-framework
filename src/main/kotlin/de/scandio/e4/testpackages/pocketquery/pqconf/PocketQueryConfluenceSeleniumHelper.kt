package de.scandio.e4.testpackages.pocketquery.pqconf

import de.scandio.e4.clients.WebConfluence
import de.scandio.e4.helpers.DomHelper
import de.scandio.e4.testpackages.pocketquery.PocketQuerySeleniumHelper
import de.scandio.e4.worker.interfaces.WebClient

class PocketQueryConfluenceSeleniumHelper(
        webClient: WebClient,
        dom: DomHelper
) : PocketQuerySeleniumHelper(webClient, dom) {


    fun insertPocketQueryMacro(outerQueryName: String) {
        val webConfluence = webClient as WebConfluence
        webConfluence.openMacroBrowser("pocketquery", "pocketquery")
        setQueryInMacroBrowser(outerQueryName)
        webConfluence.saveMacroBrowser()
    }

    fun setQueryInMacroBrowser(queryName: String) {
        setSelect2Option("#query-name-select", queryName)
    }
}