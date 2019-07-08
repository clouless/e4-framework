package de.scandio.e4.testpackages.pocketquery

import de.scandio.e4.E4
import de.scandio.e4.testpackages.TestPackageTestRun
import de.scandio.e4.testpackages.pocketquery.pqconf.PocketQueryJiraTestPackage
import de.scandio.e4.worker.interfaces.TestPackage
import org.junit.Before
import org.junit.Test

class PocketQueryJiraTestRun : TestPackageTestRun() {

    private val TEST_PACKAGE = PocketQueryJiraTestPackage()

    @Before
    fun before() {
        // noop currently
    }

    @Test
    fun runTest() {
        if (E4.PREPARATION_RUN) {
            executeTestPackagePrepare(TEST_PACKAGE)
        } else {
//            executeTestPackage(TEST_PACKAGE)

            // Run a single action for testing:
//            executeAction(CreatePageAction("LT", "macros", "<p>macro pages</p>", false))

            // Run multiple actions testing:
//             executeActions(TEST_PACKAGE.setupActions)
        }
    }

}