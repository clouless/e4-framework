package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.Test
import java.util.concurrent.TimeoutException

class DisableHipchatIntegration : BaseSeleniumTest() {

    @Test
    fun test() {
        try {
            webConfluence.login()
            webConfluence.authenticateAdmin()
            webConfluence.disablePlugin("com.atlassian.plugins.base-hipchat-integration-plugin")
        } catch (e: TimeoutException) {
            shot()
        }

    }

}