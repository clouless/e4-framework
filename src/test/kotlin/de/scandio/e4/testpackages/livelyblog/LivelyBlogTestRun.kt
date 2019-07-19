package de.scandio.e4.testpackages.livelytheme

import de.scandio.e4.E4TestEnv
import de.scandio.e4.testpackages.TestPackageTestRun
import de.scandio.e4.testpackages.livelyblogs.LivelyBlogTestPackage
import org.junit.Before
import org.junit.Test

class LivelyBlogTestRun : TestPackageTestRun() {

    private val TEST_PACKAGE = LivelyBlogTestPackage()

    @Before
    fun before() {
        // noop currently
    }

    @Test
    fun runTest() {
        if (E4TestEnv.PREPARATION_RUN) {
            executeTestPackagePrepare(TEST_PACKAGE)
        } else {
//            executeTestPackage(TEST_PACKAGE)

            // Run a single action for testing:
//            executeAction(CreatePageAction("LT", "macros", "<p>macro pages</p>", false))

            // Run single virtual user for testing:
//             executeActions(TEST_PACKAGE.getSystemSetupActions())
        }
    }

}