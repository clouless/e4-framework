package de.scandio.e4.setup

import de.scandio.e4.BaseSeleniumTest
import org.junit.Test
import java.util.concurrent.TimeoutException

class DisableSecureAdminSessions : BaseSeleniumTest() {

    @Test
    fun test() {
        try {
            webConfluence.disableSecureAdminSessions()
        } catch (e: TimeoutException) {
            shot()
        }
    }
}