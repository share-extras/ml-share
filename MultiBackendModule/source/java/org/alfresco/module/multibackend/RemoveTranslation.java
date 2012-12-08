package org.alfresco.module.multibackend;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.ml.MultilingualContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

public class RemoveTranslation extends AbstractWebScript {
	private static final Log logger = LogFactory.getLog(RemoveTranslation.class);
	// <url>multilingual/removetranslation/nodeRef={nodeRef}</url>
	private final String KEY_ARG_NODE_REF = "nodeRef";
	
	
	private MultilingualContentService multilingualContentService;




	public MultilingualContentService getMultilingualContentService() {
		return multilingualContentService;
	}

	public void setMultilingualContentService(
			MultilingualContentService multilingualContentService) {
		this.multilingualContentService = multilingualContentService;
	}

	@Override
	public void execute(WebScriptRequest req, WebScriptResponse res)
			throws IOException {
		try {
	        String docNodeRefStr = req.getParameter(KEY_ARG_NODE_REF);
	        if (docNodeRefStr == null || docNodeRefStr.length() == 0) {
	            throw new WebScriptException("Document nodeRef parameter cannot be blank");
	        }
	        NodeRef docNodeRef = new NodeRef(docNodeRefStr);
	        multilingualContentService.unmakeTranslation(docNodeRef);
	        
		} catch (Exception e) {
			throw new WebScriptException("Unable to RemoveTranslation because:" + e.getMessage());
		}
	}

}
