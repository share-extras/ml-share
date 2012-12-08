package org.alfresco.module.multibackend;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.dictionary.ClassDefinition;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.ml.MultilingualContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

public class GetTranslations extends AbstractWebScript {
	private static final Log logger = LogFactory.getLog(GetTranslations.class);
	// <url>multilingual/gettranslations/node/nodeRef={nodeRef}</url>
	private final String KEY_PARAM_NODE_REF = "nodeRef";

	
	private MultilingualContentService multilingualContentService;
	private NodeService nodeService;
	private DictionaryService dictionaryService;
	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	private NamespaceService namespaceService;
	private PermissionService permissionService;

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public void setNamespaceService(NamespaceService namespaceService) {
		this.namespaceService = namespaceService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

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
			String paramNodeRef = req.getParameter(KEY_PARAM_NODE_REF);
			NodeRef nodeRef = new NodeRef(paramNodeRef);

			
			boolean isTranslation = multilingualContentService.isTranslation(nodeRef);
			JSONObject jObj = new JSONObject();
			JSONObject container =  new JSONObject();
			if(isTranslation == true)
			{
				// build a json object
				JSONArray arrayObj=new JSONArray();
				jObj.put("items", arrayObj);

				Map<Locale,NodeRef> translations = multilingualContentService.getTranslations(nodeRef);
				for(Locale loc:translations.keySet())
				{
		            /*{
		                "type": "cm:content", -
		                "parentType": "cm:cmobject", -
		                "isContainer": false, -
		                "name": "2012;u10=Berlin;u11=DE;u12=Schifflange;u13=LU;u14=null;u17=582.00;u15=VI;u16=EUR;qty=1;cost=752.html", -
		                "title": "", -
		                "description": "", -
		                "modified": "2012-10-19T14:49:22.639+02:00", -
		                "modifier": "admin", -
		                
		                "displayPath": "\/Company Home\/test5", -
		                "nodeRef": "workspace://SpacesStore/43290b5b-26ba-49a1-92d4-7582e7c3931e", -
		                "selectable" : true, -
		                "language" : "en" 
		            }*/
					
					NodeRef nodeRefTranslation = translations.get(loc);
					JSONObject jObjTranslation = new JSONObject();
					QName longType =  nodeService.getType(nodeRefTranslation);
					jObjTranslation.put("type", getShortQName(longType));
					ClassDefinition clazz = dictionaryService.getClass(longType).getParentClassDefinition();	
					jObjTranslation.put("parentType", getShortQName(clazz.getName()));
					jObjTranslation.put("isContainer", false);
					jObjTranslation.put(ContentModel.PROP_NAME.getLocalName(),nodeService.getProperty(nodeRefTranslation, ContentModel.PROP_NAME));
					jObjTranslation.put("title", nodeService.getProperty(nodeRefTranslation, ContentModel.PROP_TITLE));
					jObjTranslation.put("description", nodeService.getProperty(nodeRefTranslation, ContentModel.PROP_DESCRIPTION));
					jObjTranslation.put("modified", nodeService.getProperty(nodeRefTranslation, ContentModel.PROP_MODIFIED));
					jObjTranslation.put("modifier", nodeService.getProperty(nodeRefTranslation, ContentModel.PROP_MODIFIER));
					String displayPath = this.nodeService.getPath(nodeRefTranslation).toDisplayPath(nodeService, permissionService);
					jObjTranslation.put("displayPath", nodeService.getProperty(nodeRefTranslation, ContentModel.PROP_MODIFIER));
					jObjTranslation.put("nodeRef", nodeRefTranslation.toString());
					jObjTranslation.put("selectable", true);
					jObjTranslation.put("language", nodeService.getProperty(nodeRefTranslation,ContentModel.PROP_LOCALE).toString());

					arrayObj.put(jObjTranslation);
				}
				container.put("data", jObj);
			}
			// build a json object
			String jsonString = container.toString();
			res.getWriter().write(jsonString);
		} catch (Exception e) {
			throw new WebScriptException("Unable to serialize JSON");
		}
	}
	
    /**
     * Given a long-form QName, this method uses the namespace service to create a
     * short-form QName string.
     * 
     * @param longQName
     * @return the short form of the QName string, e.g. "cm:content"
     */
    protected String getShortQName(QName longQName)
    {
        return longQName.toPrefixString(namespaceService);
    }
    


}
