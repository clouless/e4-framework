package de.scandio.e4.worker.rest;

import java.util.List;

public class RestJira extends RestAtlassian {

	public RestJira(String baseUrl, String username, String password) {
		super(baseUrl, username, password);
	}

	@Override
	public List<String> getUsernames() {
		// TODO
		return null;
	}

}
