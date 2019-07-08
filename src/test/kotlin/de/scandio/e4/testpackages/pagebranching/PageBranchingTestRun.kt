package de.scandio.e4.testpackages.pagebranching

import de.scandio.e4.E4
import de.scandio.e4.testpackages.TestPackageTestRun
import de.scandio.e4.worker.interfaces.TestPackage
import org.junit.Before
import org.junit.Test

class PageBranchingTestRun : TestPackageTestRun() {

    private val TEST_PACKAGE = PageBranchingTestPackage()

    @Before
    fun before() {
        // noop currently
    }

    @Test
    fun runTest() {
        if (E4.PREPARATION_RUN) {
            executeTestPackagePrepare(TEST_PACKAGE)
        } else {
            executeTestPackage(TEST_PACKAGE)

            // Run a single action for testing:
            // executeAction(CreatePageAction("MYSPACEKEY", "MYPAGETITLE"))

            // Run single virtual user for testing:
            // executeActions(BranchCreator().actions)
        }
    }

}