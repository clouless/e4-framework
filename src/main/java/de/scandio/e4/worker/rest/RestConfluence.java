package de.scandio.e4.worker.rest;

import com.google.gson.Gson;
import de.scandio.e4.worker.util.WorkerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestConfluence extends RestAtlassian {

	private static final Logger log = LoggerFactory.getLogger(RestConfluence.class);

	private static final Gson GSON = new Gson();

	private Map<String, Long> contentIds = new HashMap<>();

	public RestConfluence(String baseUrl, String username, String password) {
		super(baseUrl, username, password);
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

}
