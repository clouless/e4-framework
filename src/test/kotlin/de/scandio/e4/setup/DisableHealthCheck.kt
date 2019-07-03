package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.Test
import java.util.concurrent.TimeoutException

class DisableHealthCheck : BaseSeleniumTest() {

    @Test
    fun test() {
        try {
            webConfluence.disablePlugin("com.atlassian.troubleshooting.plugin-confluence")
        } catch (e: TimeoutException) {
            shot()
        }

    }

}