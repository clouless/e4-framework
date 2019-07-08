package de.scandio.e4.worker.rest;

import de.scandio.e4.E4;
import de.scandio.e4.worker.interfaces.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RestJira implements RestClient {

	private static final Logger log = LoggerFactory.getLogger(RestJira.class);


	private String username;
	private String password;
	private String baseUrl;

	public RestJira(String username, String password) {
		this.baseUrl = E4.REST_BASE_URL;
		this.username = username;
		this.password = password;
	}

	@Override
	public String getUser() {
		return this.username;
	}

	@Override
	public List<String> getUsernames() {
		return null;
	}

}
