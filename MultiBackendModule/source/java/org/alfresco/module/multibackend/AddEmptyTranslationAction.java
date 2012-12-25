package org.alfresco.module.multibackend;

import java.util.List;
import java.util.Locale;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.ml.MultilingualContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddEmptyTranslationAction extends ActionExecuterAbstractBase {

	private static Log logger = LogFactory.getLog(ActionExecuterAbstractBase.class);
	public static final String PARAM_LANGUAGE_LABEL = "makemultilingual.field.language";
	public static final String PARAM_LANGUAGE = "language";

	private NodeService nodeService;
	private MultilingualContentService multilingualContentService;

	public void setMultilingualContentService(MultilingualContentService multilingualContentService) {
		this.multilingualContentService = multilingualContentService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {

		paramList.add(new ParameterDefinitionImpl(PARAM_LANGUAGE, DataTypeDefinition.TEXT, true,
				getParamDisplayLabel(PARAM_LANGUAGE_LABEL)));
	}

	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		if (logger.isDebugEnabled()) {
			logger.debug("Action upon NodeRef in Add empty translation:" + actionedUponNodeRef);
			logger.debug("Locale: " + (String) action.getParameterValue(PARAM_LANGUAGE));

		}

		Locale loc = new Locale((String) action.getParameterValue(PARAM_LANGUAGE));
		String fullName = (String) nodeService.getProperty(actionedUponNodeRef, ContentModel.PROP_NAME);
		String name = stripExtension(fullName)
				+ "-" + (String) action.getParameterValue(PARAM_LANGUAGE) + "." +getExtension(fullName);
		
		multilingualContentService.addEmptyTranslation(actionedUponNodeRef, name, loc);
	}
	
	static private String stripExtension (String str) {
	        // Handle null case specially.

	        if (str == null) return null;

	        // Get position of last '.'.
	        int pos = str.indexOf(".");
	        // If there wasn't any '.' just return the string as is.
	        if (pos == -1) return str;

	        // Otherwise return the string, up to the dot.
	        return str.substring(0, pos);
	    }
	
	static private String getExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        String ext="";
        int mid= str.lastIndexOf(".");
        return str.substring(mid+1,str.length());
    }

}
