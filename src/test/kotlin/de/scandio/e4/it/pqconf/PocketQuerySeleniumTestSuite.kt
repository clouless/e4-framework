package de.scandio.e4.it.pqconf

import de.scandio.e4.BaseSeleniumTest
import de.scandio.e4.clients.WebConfluence
import de.scandio.e4.testpackages.pocketquery.PocketQuerySeleniumHelper
import de.scandio.e4.testpackages.pocketquery.pqconf.PocketQueryConfluenceSeleniumHelper
import org.junit.After
import org.junit.Test
import java.util.*

class PocketQuerySeleniumTestSuite : BaseSeleniumTest() {

    @Test // 3.9.3
    fun testRenderPocketQueryMacro() {
        val webConfluence = webClient() as WebConfluence
        try {
            val wikipediaTitle = "Vancouver"
            val wikipediaUrl = "https://en.wikipedia.org/w/api.php"
            val wikipediaQuery = "?action=parse&page=$wikipediaTitle&format=json"
            val wikipediaJsonPath = "$.parse.text['*']"
            val wikipediaConverter = """
                function convert(json) {
                    var parsedJsonObject = JSON.parse(json);
                    return [{ 'wikipediaContent': parsedJsonObject }];
                }
            """.trimIndent().trimLines()
            val innerTemplate = """
                    <link rel='stylesheet' href='https://en.wikipedia.org/w/load.php?debug=false&lang=en&modules=site.styles&only=styles&skin=vector'>
                    ${'$'}result.get(0).wikipediaContent
            """.trimIndent().trimLines()

            val pqHelper = PocketQueryConfluenceSeleniumHelper(webConfluence, webConfluence.dom)
            webConfluence.login()
            pqHelper.goToPocketQueryAdmin()
            val wikipediaDatasourceName = pqHelper.createRestCustomDatasource("WikipediaDS", wikipediaUrl)
            val wikipediaQueryName = pqHelper.createRestQuery(wikipediaDatasourceName,
                    "WikipediaNestedQuery", wikipediaQuery, wikipediaJsonPath)

            val wikipediaTemplateName = pqHelper.createTemplate("WikipediaTemplate", innerTemplate)
            val wikipediaConverterName = pqHelper.createConverter("WikipediaConverter", wikipediaConverter)

            val outerTemplate = "\$PocketQuery.renderPocketQueryMacro('$wikipediaQueryName')"

            pqHelper.setTemplateOnQuery(wikipediaQueryName, wikipediaTemplateName)
            pqHelper.setConverterOnQuery(wikipediaQueryName, wikipediaConverterName)

            val outerTemplateName = pqHelper.createTemplate("RenderConfluenceMacro", outerTemplate)
            val outerQueryName = pqHelper.createRestQuery(wikipediaDatasourceName, "WikipediaQuery",
                    wikipediaQuery, wikipediaJsonPath)
            pqHelper.setTemplateOnQuery(outerQueryName, outerTemplateName)
            pqHelper.setConverterOnQuery(outerQueryName, wikipediaConverterName)

            val pageTitle = "PQ RenderConfluenceMacro (${Date().time})"
            webConfluence.createDefaultPage("PQ", pageTitle)
            webConfluence.goToEditPage()
            pqHelper.insertPocketQueryMacro(outerQueryName)
            webConfluence.savePage()
            dom.awaitElementPresent(".pocketquery-result .mw-parser-output", 40)
        } catch (e: Exception) {
            shot()
            throw e
        } finally {
            shot()
        }
    }

    @After
    fun after() {
        webClient().quit()
    }

    fun String.trimLines() = replace("\n", "")

}