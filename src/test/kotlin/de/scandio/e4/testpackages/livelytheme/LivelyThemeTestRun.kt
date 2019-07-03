package de.scandio.e4.testpackages.livelytheme

import de.scandio.e4.testpackages.TestPackageTestRun
import de.scandio.e4.worker.interfaces.TestPackage
import org.junit.Before
import org.junit.Test

class LivelyThemeTestRun : TestPackageTestRun() {

    private val BASE_URL = "http://confluence-cluster-6153-lb:26153/"
//    private val BASE_URL = "http://e4-test:8090/"
    private val OUT_DIR = System.getenv("E4_OUTPUT_DIR")
    private val IN_DIR = System.getenv("E4_INPUT_DIR")
    private val USERNAME = "admin"
    private val PASSWORD = "admin"
    private val TEST_PACKAGE = LivelyThemeTestPackage()
    private val PREPARATION_RUN = false

    @Before
    fun before() {
        // noop currently
    }

    @Test
    fun runTest() {
        if (PREPARATION_RUN) {
            executeTestPackagePrepare(TEST_PACKAGE)
        } else {
//            executeTestPackage(TEST_PACKAGE)

            // Run a single action for testing:
//            executeAction(CreatePageAction("LT", "macros", "<p>macro pages</p>", false))

            // Run single virtual user for testing:
             executeActions(TEST_PACKAGE.getSystemSetupActions())
        }
    }

    override fun getBaseUrl(): String { return BASE_URL }
    override fun getOutDir(): String { return OUT_DIR }
    override fun getInDir(): String { return IN_DIR }
    override fun getUsername(): String { return USERNAME }
    override fun getPassword(): String { return PASSWORD }
    override fun getTestPackage(): TestPackage { return TEST_PACKAGE }

}