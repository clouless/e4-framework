package de.scandio.e4.testpackages.livelytheme

import de.scandio.e4.testpackages.vanilla.actions.CreatePageAction
import de.scandio.e4.testpackages.vanilla.actions.CreateSpaceAction
import de.scandio.e4.testpackages.vanilla.virtualusers.*
import de.scandio.e4.worker.collections.ActionCollection
import de.scandio.e4.worker.interfaces.TestPackage
import de.scandio.e4.worker.collections.VirtualUserCollection

/**
 * === LivelyThemeTestPackage ===
 *
 * Test package for Lively Theme Confluence virtual users.
 *
 * Assumptions:
 * TODO
 *
 * Setup:
 * TODO
 *
 * Virtual Users:
 * TODO
 *
 *
 * @author Felix Grund
 */
class LivelyThemeTestPackage: TestPackage {

    val DASHBOARD_CONTENT = """
<ac:layout><ac:layout-section ac:type="two_equal"><ac:layout-cell>
<p><ac:structured-macro ac:name="recently-updated" ac:schema-version="1" ac:macro-id="26d1ebca-3c09-401c-b940-4a76ac59feeb" /></p></ac:layout-cell><ac:layout-cell>
<p><ac:structured-macro ac:name="blog-posts" ac:schema-version="1" ac:macro-id="29ac3193-cda9-459d-9db9-ee82e5d1687c" /></p></ac:layout-cell></ac:layout-section></ac:layout>
    """.trimIndent()

    override fun getSetupActions(): ActionCollection {
        val actions = ActionCollection()
        actions.add(CreateSpaceAction("LT", "Lively Theme"))
        actions.add(CreatePageAction("LT", "dashboard", DASHBOARD_CONTENT, "Lively Theme Home", true))
        return actions
    }

    override fun getVirtualUsers(): VirtualUserCollection {
        val virtualUsers = VirtualUserCollection()
        virtualUsers.add(Commentor::class.java, 0.08)
        virtualUsers.add(Reader::class.java, 0.36)
        virtualUsers.add(Creator::class.java, 0.08)
        virtualUsers.add(Searcher::class.java, 0.16)
        virtualUsers.add(Editor::class.java, 0.16)
        virtualUsers.add(Dashboarder::class.java, 0.16)
        return virtualUsers
    }

}