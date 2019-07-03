package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertTrue

class InstallPageBranching : BaseSeleniumTest() {

    val PLUGIN_NAME = "page-branching"
    val PLUGIN_VERSION = "1.2.0"
    val PLUGIN_KEY = "de.scandio.confluence.plugins.page-branching"
    val PLUGIN_LICENSE = System.getenv("E4_LICENSE_PB")

    @Test
    fun test() {
        try {
            webConfluence.login()
            webConfluence.authenticateAdmin()
            webConfluence.installPlugin(PLUGIN_NAME, PLUGIN_VERSION, PLUGIN_KEY, PLUGIN_LICENSE)
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