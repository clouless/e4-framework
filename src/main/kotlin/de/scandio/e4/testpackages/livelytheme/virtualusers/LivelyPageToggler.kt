package de.scandio.e4.testpackages.livelytheme.virtualusers

import de.scandio.e4.testpackages.livelytheme.actions.CreateRandomLivelyThemeMacroPage
import de.scandio.e4.testpackages.vanilla.actions.FavPageToggleAction
import de.scandio.e4.worker.collections.ActionCollection
import de.scandio.e4.worker.interfaces.VirtualUser


/**
 * === LivelyPageToggler ===
 *
 * TODO
 *
 * @author Felix Grund
 */
open class LivelyPageToggler : VirtualUser() {

    override fun getActions(): ActionCollection {
        val actions = ActionCollection()
        actions.add(FavPageToggleAction())
        return actions
    }

}