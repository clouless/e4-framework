package de.scandio.e4.testpackages.pqconf

import de.scandio.e4.testpackages.vanilla.actions.InstallPluginAction
import de.scandio.e4.worker.collections.ActionCollection
import de.scandio.e4.worker.collections.VirtualUserCollection
import de.scandio.e4.worker.interfaces.TestPackage

/**
 * === PocketQuery for Confluence Test Package ===
 *
 * TODO: docs!
 *
 * @author Felix Grund
 */
class PocketQueryConfTestPackage: TestPackage {

    override fun getVirtualUsers(): VirtualUserCollection {
        val virtualUsers = VirtualUserCollection()

        return virtualUsers
    }

    override fun getSetupActions(): ActionCollection {
        val actions = ActionCollection()
        actions.add(InstallPluginAction("pocketquery", "3.9.2"))

        return actions
    }

}