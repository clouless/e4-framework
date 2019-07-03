package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertTrue

class InstallPocketQueryConf : BaseSeleniumTest() {

    val PLUGIN_NAME = "pocketquery"
    val PLUGIN_VERSION = "3.9.2"
    val PLUGIN_KEY = "de.scandio.confluence.plugins.pocketquery"
    val PLUGIN_LICENSE = System.getenv("E4_LICENSE_PQCONF")

    @Test
    fun test() {
        try {
            webConfluence.login()
            webConfluence.authenticateAdmin()
            webConfluence.installPlugin(PLUGIN_NAME, PLUGIN_VERSION, PLUGIN_LICENSE, PLUGIN_KEY)
            assertTrue(dom.isElementPresent("#upm-plugin-status-dialog") || dom.isElementPresent(".upm-plugin-license-edit"))
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