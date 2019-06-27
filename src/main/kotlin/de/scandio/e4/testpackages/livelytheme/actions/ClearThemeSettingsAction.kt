package de.scandio.e4.testpackages.livelytheme.actions

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.worker.interfaces.Action
import de.scandio.e4.worker.interfaces.RestClient
import de.scandio.e4.worker.interfaces.WebClient
import org.openqa.selenium.Dimension
import java.util.*

/**
 * @author Felix Grund
 */
open class ClearThemeSettingsAction : Action() {

    protected var start: Long = 0
    protected var end: Long = 0

    override fun execute(webClient: WebClient, restClient: RestClient) {
        val webConfluence = webClient as WebConfluence
        val dom = webConfluence.dom
        webConfluence.login()
        this.start = Date().time
        webConfluence.navigateTo("admin/plugins/lively/theme/settings.action")
        dom.awaitElementClickable("#dashboardPage")
        dom.unselectAll("form.aui input.checkbox[checked='checked']")
        arrayOf("dashboard", "header", "footer", "menu", "submenu").forEach {
            dom.clearText("#${it}Page")
        }
        dom.awaitSeconds(3)
        dom.click("form.aui .aui-button.submit")
        dom.awaitElementVisible("form.aui .buttons span.success")
        this.end = Date().time
    }

    override fun getTimeTaken(): Long {
        return this.end - this.start
    }

}