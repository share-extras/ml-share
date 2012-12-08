package org.alfresco.module.multibackend;

import java.io.Serializable;
import java.util.Locale;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.jscript.app.BasePropertyDecorator;
import org.alfresco.service.cmr.ml.MultilingualContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Enrich the answer with the pivot language. 
 * @author philippe
 *
 */
public class MultilingualPropertyDecorator extends BasePropertyDecorator {
	private MultilingualContentService multilingualContentService;
	private NodeService nodeService;

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}


	public void setMultilingualContentService(
			MultilingualContentService multilingualContentService) {
		this.multilingualContentService = multilingualContentService;
	}


	public JSONAware decorate(QName propertyName, NodeRef nodeRef,Serializable value) {
		JSONObject map = new JSONObject();
		
		//if node is not multilingual, just return an empty map
		if(!multilingualContentService.isTranslation(nodeRef))
		{
			Locale nodeLanguage = (Locale)nodeService.getProperty(nodeRef, ContentModel.PROP_LOCALE);
			if(nodeLanguage != null)
			{
			   map.put("value",nodeLanguage.getLanguage());
			   map.put("ispivotlanguage",false);
			}
			return map;
		}
		NodeRef pivotNodeRef = multilingualContentService.getPivotTranslation(nodeRef);
		Locale pivotLanguage = (Locale)nodeService.getProperty(pivotNodeRef, ContentModel.PROP_LOCALE);
		map.put("pivotlanguage", pivotLanguage.getLanguage());
		Locale nodeLanguage = (Locale)nodeService.getProperty(nodeRef, ContentModel.PROP_LOCALE);
		map.put("value",nodeLanguage.getLanguage());
		map.put("ispivotlanguage", pivotLanguage.getLanguage().equals(nodeLanguage.getLanguage())?true:false);	
		
		return map;
	}

}
