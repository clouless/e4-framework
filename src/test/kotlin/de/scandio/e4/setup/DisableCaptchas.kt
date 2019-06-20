package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.Test
import java.util.concurrent.TimeoutException

class DisableCaptchas : BaseSeleniumTest() {

    @Test
    fun test() {
        try {
            webConfluence.disableCaptchas()
        } catch (e: TimeoutException) {
            shot()
        }

    }
}