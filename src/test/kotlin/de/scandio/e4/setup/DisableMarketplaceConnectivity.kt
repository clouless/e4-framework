package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.Test

class DisableMarketplaceConnectivity : BaseSeleniumTest() {

    @Test
    fun test() {
        try {
            webConfluence.login()
            webConfluence.authenticateAdmin()
            webConfluence.navigateTo("plugins/servlet/upm")
            dom.click("#link-bar-settings a", 30)
            dom.click("#upm-checkbox-pacDisabled", 30)
            dom.click("#upm-settings-dialog .aui-button.confirm")
            shot()
        } catch (e: Exception) {
            shot()
        }

    }

}