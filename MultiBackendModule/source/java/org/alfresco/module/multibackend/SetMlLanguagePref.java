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
/**
 * This is just a dummy webscript. The setting of the cookie is made by LakguageCookieFilter
 * @author philippe
 *
 */
public class SetMlLanguagePref extends AbstractWebScript {
	private static final Log logger = LogFactory.getLog(SetMlLanguagePref.class);




	@Override
	public void execute(WebScriptRequest req, WebScriptResponse res)
			throws IOException {
		try {			
			JSONObject jObj = new JSONObject();
			jObj.put("status", "OK");
			// build a json object
			String jsonString = jObj.toString();
			res.getWriter().write(jsonString);
		} 
		catch (WebScriptException we)
		{
			throw we;
		}
		catch (Exception e) {
			throw new WebScriptException("Unable to serialize JSON",e);
		}
	}

}
