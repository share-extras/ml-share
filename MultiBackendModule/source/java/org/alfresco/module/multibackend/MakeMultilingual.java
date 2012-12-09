package org.alfresco.module.multibackend;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.ml.MultilingualContentService;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

public class MakeMultilingual extends AbstractWebScript {
	private static final Log logger = LogFactory.getLog(MakeMultilingual.class);
	// <url>multilingual/gettranslations/node/{store_type}/{store_id}/{id}</url>
	private final String KEY_ARG_STORE_TYPE = "store_type";
	private final String KEY_ARG_STORE_ID = "store_id";
	private final String KEY_ARG_ID = "id";

	MultilingualContentService multilingualContentService;
	NodeService nodeService;

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setMultilingualContentService(MultilingualContentService multilingualContentService) {
		this.multilingualContentService = multilingualContentService;
	}

	@Override
	public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
		try {
			// Parse the JSON, if supplied
			JSONObject json = null;
			String contentType = req.getContentType();
			if (contentType != null && contentType.indexOf(';') != -1) {
				contentType = contentType.substring(0, contentType.indexOf(';'));
			}
			if (MimetypeMap.MIMETYPE_JSON.equals(contentType)) {
				JSONParser parser = new JSONParser();
				try {
					json = (JSONObject) parser.parse(req.getContent().getContent());
				} catch (IOException io) {
					throw new WebScriptException(Status.STATUS_BAD_REQUEST, "Invalid JSON: "
							+ io.getMessage());
				} catch (ParseException pe) {
					throw new WebScriptException(Status.STATUS_BAD_REQUEST, "Invalid JSON: "
							+ pe.getMessage());
				}
			}
			// if already multilingual then does noting
			JSONObject jObj = new JSONObject();
			Locale loc = new Locale(json.getString("locale"));
			NodeRef nodeRef = new NodeRef(json.getString("protocol"), json.getString("identifier"),
					json.getString("nodeRef"));
			if (multilingualContentService.isTranslation(nodeRef)) {
				jObj.put("ALDREADY_TRANSLATION", "true");
			} else {
				multilingualContentService.makeTranslation(nodeRef, loc);
				// update content url to stick with ContentModel.PROP_LOCALE It
				// is
				// important when indexing
				ContentData content = (ContentData) nodeService.getProperty(nodeRef,
						ContentModel.PROP_CONTENT);
				if (content != null) {
					// ContentData(String contentUrl, String mimetype, long
					// size,
					// String encoding, Locale locale)
					ContentData updateContentWithLocale = new ContentData(content.getContentUrl(),
							content.getMimetype(), content.getSize(), content.getEncoding(), loc);
					nodeService.setProperty(nodeRef, ContentModel.PROP_CONTENT, updateContentWithLocale);
				}
			}
			jObj.put("STATUS", "OK");
			String jsonString = json.toString();
			res.getWriter().write(jsonString);
		} catch (Exception e) {
			throw new WebScriptException("Unable to serialize JSON");
		}
	}

}
