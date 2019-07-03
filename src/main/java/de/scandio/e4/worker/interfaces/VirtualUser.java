package de.scandio.e4.worker.interfaces;

import de.scandio.e4.worker.collections.ActionCollection;

/**
 * One instance of Selenium.
 * Represents a user when thinking about user load.
 *
 * The actions should be run in a separate thread.
 */
public abstract class VirtualUser {
	public abstract ActionCollection getActions();

	public boolean isAdminRequired() {
		return false;
	}
}
