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
    private val PREPARATION_RUN = false

    val DASHBOARD_CONTENT = """
<ac:layout><ac:layout-section ac:type="two_equal"><ac:layout-cell>
<p><ac:structured-macro ac:name="recently-updated" ac:schema-version="1" ac:macro-id="26d1ebca-3c09-401c-b940-4a76ac59feeb" /></p></ac:layout-cell><ac:layout-cell>
<p><ac:structured-macro ac:name="blog-posts" ac:schema-version="1" ac:macro-id="29ac3193-cda9-459d-9db9-ee82e5d1687c" /></p></ac:layout-cell></ac:layout-section></ac:layout>
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