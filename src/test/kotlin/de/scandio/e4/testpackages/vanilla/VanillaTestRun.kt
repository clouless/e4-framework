package de.scandio.e4.testpackages.vanilla

import de.scandio.e4.E4
import de.scandio.e4.testpackages.TestPackageTestRun
import de.scandio.e4.testpackages.pagebranching.actions.CreateBranchAction
import de.scandio.e4.testpackages.pagebranching.actions.CreateOverviewPageAction
import de.scandio.e4.testpackages.pagebranching.actions.MergeBranchAction
import de.scandio.e4.testpackages.pagebranching.virtualusers.*
import de.scandio.e4.testpackages.vanilla.actions.*
import de.scandio.e4.worker.collections.ActionCollection
import de.scandio.e4.worker.interfaces.TestPackage
import org.junit.Before
import org.junit.Test
import java.util.*

class VanillaTestRun : TestPackageTestRun() {

    private val TEST_PACKAGE = VanillaTestPackage()

    @Before
    fun before() {
        //noop currently
    }

    @Test
    fun runTest() {
        if (E4.PREPARATION_RUN) {
            executeTestPackagePrepare(TEST_PACKAGE)
        } else {
//                executeTestPackage(TEST_PACKAGE)

            // Run a single action for testing:
             executeAction(AddRandomCommentAction())

            // Run single virtual user for testing:
            // executeActions(BranchCreator().actions)
        }
    }

}