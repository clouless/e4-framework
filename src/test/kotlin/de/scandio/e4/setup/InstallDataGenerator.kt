package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.After
import org.junit.Test

class InstallDataGenerator : BaseSeleniumTest() {

    val PLUGIN_NAME = "data-generator"
    val PLUGIN_VERSION = "LATEST"

    @Test
    fun test() {
        webConfluence.installPlugin(PLUGIN_NAME, PLUGIN_VERSION)
    }

    @After
    fun after() {
        webConfluence.quit()
    }

}