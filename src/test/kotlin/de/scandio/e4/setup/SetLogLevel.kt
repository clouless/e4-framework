package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.Test

class SetLogLevel : BaseSeleniumTest() {

    @Test
    fun test() {
        try {
            webConfluence.setLogLevel("co.goodsoftware", "INFO")
            shot()
        } catch (e: Exception) {
            shot()
        }

    }

}