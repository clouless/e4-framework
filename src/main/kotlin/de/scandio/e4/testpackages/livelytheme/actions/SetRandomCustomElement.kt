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
open class SetRandomCustomElement(
        val spaceKeyForThemePages: String,
        val startPageTitle: String
) : Action() {

    protected var start: Long = 0
    protected var end: Long = 0

    private var webConfluence: WebConfluence? = null

    override fun execute(webClient: WebClient, restClient: RestClient) {
        val webConfluence = webClient as WebConfluence
        this.webConfluence = webConfluence
        val dom = webConfluence.dom
        webConfluence.login()
        this.start = Date().time
        webConfluence.navigateTo("admin/plugins/lively/theme/settings.action")
        dom.awaitElementClickable("#dashboardPage")
        dom.unselectAll("form.aui input.checkbox[checked='checked']")
        val customElementsList = arrayListOf("dashboard", "header", "footer", "menu", "submenu")
        customElementsList.forEach {
            dom.clearText("#${it}Page")
        }

        customElementsList.add("favouritesMenu")
        val randomCustomElement = WorkerUtils.getRandomItem(customElementsList)
        dom.click("#${randomCustomElement}Enabled")
        dom.awaitSelected("#${randomCustomElement}Enabled")

        dom.click("form.aui .aui-button.submit")
        dom.awaitElementVisible("form.aui .buttons span.success")

        if ("dashboard".equals(randomCustomElement)) {
            webConfluence.goToDashboard()
        } else {
            webConfluence.goToPage(spaceKeyForThemePages, startPageTitle)
        }

        // in this case we need to open the dropdown first
        if ("menu".equals(randomCustomElement)) {
            dom.click("#lively-custom-menu-link")
        }

        // if it's not the favorites menu we need to create a custom element page
        if (!"favouritesMenu".equals(randomCustomElement)) {
            val setContentAndSave = !"dashboard".equals(randomCustomElement)
            createCustomElementPage(randomCustomElement, setContentAndSave)
        }

        // if it's the dashboard element we need to be a bit more sophisticated with the page content
        if ("dashboard".equals(randomCustomElement)) {
            webConfluence.clearEditorContent()
            webConfluence.setTwoColumnLayout()
            webConfluence.insertMacro("lively-dashboard-element", "Confluence Element",
                    mapOf("element" to "all-updates"))
            webConfluence.insertMacro("lively-dashboard-element", "Confluence Element",
                    mapOf("element" to "welcome-message"))
            webConfluence.savePage()
        }

        this.end = Date().time
    }

    private fun createCustomElementPage(customElementKey: String, setContentAndSave: Boolean) {
        val webConfluence = webConfluence!!
        val dom = webConfluence.dom
        val customElementPageTitle = "$customElementKey (${Date().time})"
        dom.click(".lively-custom-$customElementKey .lively-custom-element-edit-button")
        dom.awaitElementVisible("input#pageTitle")
        dom.insertText("#spaceKey", spaceKeyForThemePages, true)
        dom.insertText("#pageTitle", customElementPageTitle, true)
        dom.click("#create-$customElementKey-page-dialog button.submit")
        webConfluence.awaitEditPageLoaded()
        webConfluence.takeScreenshot("TEST")
        if (setContentAndSave) {
            dom.insertTextTinyMce("<p>Auto generated $customElementKey page</p>")
            webConfluence.savePage()
        }
    }

    override fun getTimeTaken(): Long {
        return this.end - this.start
    }

}