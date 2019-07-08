package de.scandio.e4.testpackages.livelytheme

import de.scandio.e4.E4
import de.scandio.e4.testpackages.TestPackageTestRun
import de.scandio.e4.worker.interfaces.TestPackage
import org.junit.Before
import org.junit.Test

class LivelyThemeTestRun : TestPackageTestRun() {

    private val TEST_PACKAGE = LivelyThemeTestPackage()

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

            // Run single virtual user for testing:
             executeActions(TEST_PACKAGE.getSystemSetupActions())
        }
    }

}