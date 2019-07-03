package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.After
import org.junit.Test

class PocketQuerySetupTest : BaseSeleniumTest() {

    @Test
    fun test() {
        try {

        } catch (e: Exception) {
            shot()
            throw e
        }
    }

    @After
    fun after() {
        webConfluence.quit()
    }

}