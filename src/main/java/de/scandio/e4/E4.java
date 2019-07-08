package de.scandio.e4;

import de.scandio.e4.worker.client.ClientPlatform;
import org.apache.commons.lang3.StringUtils;

public class E4 {

	private static final String[] REQUIRED_VARS = {
			"E4_APPLICATION_TYPE", "E4_WEB_BASE_URL", "E4_REST_BASE_URL", "E4_OUT_DIR", "E4_IN_DIR", "E4_ADMIN_USER",
			"E4_ADMIN_PWD"
	};

	static {
		for (String requiredVar : REQUIRED_VARS) {
			if (StringUtils.isBlank(System.getenv(requiredVar))) {
				throw new IllegalArgumentException("Not all required environment variables are " +
						"provided. Missing '"+requiredVar+ "'. " +
						"\nAll required vars:\n"+StringUtils.join(REQUIRED_VARS, "\n"));
			}
		}
	}

	// BEGIN: REQUIRED
	public static final ClientPlatform APPLICATION_TYPE = ClientPlatform.valueOf(System.getenv("E4_APPLICATION_TYPE"));
	public static final String WEB_BASE_URL = resolveBaseUrl("E4_WEB_BASE_URL");
	public static final String REST_BASE_URL = resolveBaseUrl("E4_REST_BASE_URL");
	public static final String OUT_DIR = System.getenv("E4_OUT_DIR");
	public static final String IN_DIR = System.getenv("E4_IN_DIR");
	public static final String ADMIN_USERNAME = System.getenv("E4_ADMIN_USER");
	public static final String ADMIN_PASSWORD = System.getenv("E4_ADMIN_PWD");
	// END: REQUIRED

	public static final String APPLICATION_LICENSE = System.getenv("E4_APPLICATION_LICENSE");
	public static final boolean PREPARATION_RUN = "true".equals(System.getenv("E4_PREPARATION_RUM"));

	private static String resolveBaseUrl(String varName) {
		String url = System.getenv(varName);
		return url.endsWith("/") ? url : url + "/";
	}

}
