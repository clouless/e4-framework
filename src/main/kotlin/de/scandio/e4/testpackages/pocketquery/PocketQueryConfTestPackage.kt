package de.scandio.e4.testpackages.pocketquery

import de.scandio.e4.E4
import de.scandio.e4.testpackages.vanilla.actions.CreateSpaceAction
import de.scandio.e4.testpackages.vanilla.actions.InstallPluginAction
import de.scandio.e4.worker.collections.ActionCollection
import de.scandio.e4.worker.collections.VirtualUserCollection
import de.scandio.e4.worker.interfaces.TestPackage
import java.lang.Exception

/**
 * === PocketQuery for Confluence Test Package ===
 *
 * TODO: docs!
 *
 * @author Felix Grund
 */
class PocketQueryConfTestPackage: TestPackage {

    init {
        if (E4.APP_VERSION == null) {
            throw Exception("env var E4_APP_VERSION must be set")
        }
        if (E4.APP_LICENSE == null) {
            throw Exception("env var E4_APP_LICENSE must be set")
        }
    }

    override fun getVirtualUsers(): VirtualUserCollection {
        val virtualUsers = VirtualUserCollection()
        return virtualUsers
    }

    override fun getSetupActions(): ActionCollection {
        val actions = ActionCollection()
        //actions.add(InstallPluginAction("pocketquery", E4.APP_VERSION, E4.APP_LICENSE, "de.scandio.confluence.plugins.pocketquery"))
//        actions.add(CreateSpaceAction("PQ", "PocketQuery", true))
        return actions
    }

}