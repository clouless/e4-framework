package de.scandio.e4.testpackages.pagebranching

import de.scandio.e4.testpackages.TestPackageTestRun
import de.scandio.e4.testpackages.livelytheme.LivelyThemeTestPackage
import de.scandio.e4.testpackages.vanilla.actions.CreatePageAction
import de.scandio.e4.worker.interfaces.TestPackage
import org.junit.Before
import org.junit.Test

class LivelyThemeTestRun : TestPackageTestRun() {

//    private val BASE_URL = "http://confluence-cluster-6153-lb:26153/"
    private val BASE_URL = "http://e4-test:8090/"
    private val OUT_DIR = "/tmp/e4/out"
    private val IN_DIR = "/tmp/e4/in"
    private val USERNAME = "admin"
    private val PASSWORD = "admin"
    private val TEST_PACKAGE = LivelyThemeTestPackage()
    private val PREPARATION_RUN = true

    val DASHBOARD_CONTENT = """
        <p><ac:structured-macro ac:name="recently-updated" ac:schema-version="1" ac:macro-id="c148d3ff-5eb4-4e63-b7d1-4b27ae7b3689"><ac:parameter ac:name="max">40</ac:parameter><ac:parameter ac:name="hideHeading">true</ac:parameter><ac:parameter ac:name="theme">social</ac:parameter></ac:structured-macro></p>
    """.trimIndent()

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


            // Run single virtual user for testing:
            // executeActions(BranchCreator().actions)
        }
    }

    override fun getBaseUrl(): String { return BASE_URL }
    override fun getOutDir(): String { return OUT_DIR }
    override fun getInDir(): String { return IN_DIR }
    override fun getUsername(): String { return USERNAME }
    override fun getPassword(): String { return PASSWORD }
    override fun getTestPackage(): TestPackage { return TEST_PACKAGE }

}