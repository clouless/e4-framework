package de.scandio.e4.worker.rest;

import com.google.gson.Gson;
import de.scandio.e4.E4;
import de.scandio.e4.worker.interfaces.RestClient;
import de.scandio.e4.worker.util.WorkerUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestConfluence implements RestClient {

	private static final Logger log = LoggerFactory.getLogger(RestConfluence.class);

	private static final Gson GSON = new Gson();

	private Map<String, Long> contentIds = new HashMap<>();

	private String username;
	private String password;
	private String baseUrl;

	public RestConfluence(String username, String password) {
		this.baseUrl = E4.REST_BASE_URL;
		this.username = username;
		this.password = password;
	}

	public String findPage(String spaceKey, String title) {
		String urlAfterBaseUrl = String.format("rest/api/content?title=%s&spaceKey=%s", title, spaceKey);
		return sendGetRequestReturnBody(urlAfterBaseUrl);
	}

	public List<Long> findContentIds(int limit, String type, String spaceKey) {
		String url = "rest/api/content?start=0&limit=%s";
		if (type != null && spaceKey != null) {
			url = String.format(url + "&type=%s&spaceKey=%s", limit, type, spaceKey);
		} else if (spaceKey != null) {
			url = String.format(url + "&spaceKey=%s", limit, spaceKey);
		} else if (type != null) {
			url = String.format(url + "&type=%s", limit, type);
		} else {
			url = String.format(url, limit);
		}
		String body = sendGetRequestReturnBody(url);
		return getContentIdsFromResponse(body);
	}

	private List<Long> getContentIdsFromResponse(String body) {
		List<Long> ids = new ArrayList<>();
		List<Map> pageObjects = getResultListFromResponse(body);
		for (Map pageObj : pageObjects) {
			long id = Long.parseLong((String) pageObj.get("id"));
			ids.add(id);
		}
		return ids;
	}

	public List<Long> findPages(int limit) {
		return findContentIds(limit, "page", null);
	}

	public List<Long> findBlogposts(int limit) {
		return findContentIds(limit, "blogpost", null);
	}

	public Long getRandomContentId() {
		return getRandomContentId(null, null);
	}


	public Long getRandomContentId(String spaceKey, String parentPageTitle) {
		Long contentId;
		if (StringUtils.isBlank(parentPageTitle)) {
			contentId = WorkerUtils.getRandomItem(findContentIds(1000, null, spaceKey));
		} else {
			contentId = WorkerUtils.getRandomItem(findChildPageIds(spaceKey, parentPageTitle));
		}
		return contentId;
	}

	private List<Long> findChildPageIds(String spaceKey, String parentPageTitle) {
		Long parentContentId = contentIds.get(spaceKey + ":" + parentPageTitle);
		if (parentContentId == null) {
			parentContentId = getPageId(spaceKey, parentPageTitle);
		}
		String restUrl = "rest/api/content/"+parentContentId+"/child/page";
		String responseText = sendGetRequestReturnBody(restUrl);
		return getContentIdsFromResponse(responseText);
	}

	public List<String> getUsernames() {
		String body = sendGetRequestReturnBody("rest/api/group/confluence-users/member");
		return getListFromResponse(String.class, "username", body);
	}

	private <T> List<T> getListFromResponse(Class<T> clazz, String key, String responseText) {
		List<T> ret = new ArrayList<>();
		List<Map> maps = getResultListFromResponse(responseText);
		for (Map obj : maps) {
			T item = (T) obj.get(key);
			ret.add(item);
		}
		return ret;
	}

	public String createPage(String spaceKey, String pageTitle, String content) {
		return createPage(spaceKey, pageTitle, content, null);
	}

	public String createPage(String spaceKey, String pageTitle, String content, String parentPageTitle) {
		content = StringEscapeUtils.escapeJson(content);
		String bodyTemplate, body;
		if (StringUtils.isNotBlank(parentPageTitle)) {
			long parentPageId = getPageId(spaceKey, parentPageTitle);
			bodyTemplate = "{\"type\":\"page\",\"ancestors\":[{\"id\":%s}],\"title\":\"%s\",\"space\":{\"key\":\"%s\"},\"body\":{\"storage\":{\"value\":\"%s\",\"representation\":\"storage\"}}}";
			body = String.format(bodyTemplate, parentPageId, pageTitle, spaceKey, content);
		} else {
			bodyTemplate = "{\"type\":\"page\",\"title\":\"%s\",\"space\":{\"key\":\"%s\"},\"body\":{\"storage\":{\"value\":\"%s\",\"representation\":\"storage\"}}}";
			body = String.format(bodyTemplate, pageTitle, spaceKey, content);
		}
		return sendPostRequest("rest/api/content/", body);
	}

	public Long getPageId(String spaceKey, String pageTitle) {
		String responseText = findPage(spaceKey, pageTitle);
		List<Map> pages = getResultListFromResponse(responseText);
		return Long.parseLong((String) pages.get(0).get("id"));
	}

	public String createSpace(String spaceKey, String spaceName) {
		String spaceDesc = "Space used for testing";
//		String bodyTemplate = "{\"key\":\"%s\",\"name\":\"%s\",\"description\":{\"plain\":{\"value\":\"%s\",\"representation\":\"plain\"}},\"metadata\":{}}";
		String bodyTemplate = "{\"key\":\"%s\",\"name\":\"%s\",\"description\":{\"plain\":{\"value\":\"%s\",\"representation\":\"plain\"}},\"metadata\":{}}";
		String body = String.format(bodyTemplate, spaceKey, spaceName, spaceDesc);
		return sendPostRequest("rest/api/space/", body);
	}

	private List<Map> getResultListFromResponse(String responseText) {
		Map<String, Object> response = GSON.fromJson(responseText, Map.class);
		List<Map> pageObjects = (ArrayList) response.get("results");
		return pageObjects;
	}

	private String sendPutRequest(String urlAfterBaseUrl, String body) {
		return sendPostOrPutRequest(HttpMethod.PUT, urlAfterBaseUrl, body);
	}

	private String sendPostRequest(String urlAfterBaseUrl, String body) {
		return sendPostOrPutRequest(HttpMethod.POST, urlAfterBaseUrl, body);
	}

	private String sendPostOrPutRequest(HttpMethod method, String urlAfterBaseUrl, String body) {
		final String url = this.baseUrl + urlAfterBaseUrl;
		RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
//		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//		interceptors.add(new LoggingRequestInterceptor());
//		restTemplate.setInterceptors(interceptors);

		log.debug("Sending {{}} request {{}} with body {{}}", method, url, body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", getBasicAuth());
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<String> request = new HttpEntity<>(body, headers);

		ResponseEntity<String> response;
		try {
			response = restTemplate.exchange(url, method, request, String.class);
		} catch (HttpClientErrorException e) {
			log.error("Exception sending REST {{}} request for user {{}} with password {{}} and URL {{}}",
					method, this.username, this.password, url);
			throw e;
		}
		return response.getBody();
	}

	public int sendGetRequestReturnStatus(String urlAfterBaseUrl) {
		return sendGetRequestReturnResponse(urlAfterBaseUrl).getStatusCodeValue();
	}

	public String sendGetRequestReturnBody(String urlAfterBaseUrl) {
		ResponseEntity<String> response = sendGetRequestReturnResponse(urlAfterBaseUrl);
		String responseText = response.getBody();
//		log.debug("Response text {{}}", responseText);
		return responseText;
	}

	private ResponseEntity<String> sendGetRequestReturnResponse(String urlAfterBaseUrl) {
		final String url = this.baseUrl + urlAfterBaseUrl;
		final RestTemplate restTemplate = new RestTemplate();

		log.debug("Sending GET request {{}}", url);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", getBasicAuth());

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response;
		try {
			response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		} catch (HttpClientErrorException e) {
			log.error("Exception sending REST GET request for user {{}} with password {{}} and URL {{}}", this.username, this.password, url);
			throw e;
		}

		return response;
	}

	private String getBasicAuth() {
		String plainCreds = this.username + ":" + this.password;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		return "Basic " + base64Creds;
	}

	@Override
	public String getUser() {
		return this.username;
	}

}
