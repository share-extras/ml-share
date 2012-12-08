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

public class MakeMultilingualAction extends ActionExecuterAbstractBase {
    
    private static Log logger = LogFactory.getLog(MakeMultilingualAction.class);

    // Form parameters
    public static final String PARAM_AUTHOR = "author";
    public static final String PARAM_LANGUAGE = "language";

    public static final String PARAM_AUTHOR_LABEL = "makemultilingual.field.author";
    public static final String PARAM_LANGUAGE_LABEL = "makemultilingual.field.language";
    
    private NodeService nodeService;
	private MultilingualContentService multilingualContentService;



    public void setMultilingualContentService(
			MultilingualContentService multilingualContentService) {
		this.multilingualContentService = multilingualContentService;
	}

	public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @Override
    protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
        paramList.add(new ParameterDefinitionImpl(PARAM_AUTHOR,
                DataTypeDefinition.TEXT, true,
                getParamDisplayLabel(PARAM_AUTHOR_LABEL)));
        paramList.add(new ParameterDefinitionImpl(PARAM_LANGUAGE,
                DataTypeDefinition.TEXT, true,
                getParamDisplayLabel(PARAM_LANGUAGE_LABEL)));
    }

    @Override
    protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
    	Locale loc = new Locale((String)action.getParameterValue(PARAM_LANGUAGE));
    	String mlAuthor = (String)action.getParameterValue(PARAM_AUTHOR);
    	multilingualContentService.makeTranslation(actionedUponNodeRef, loc);
        
    	// if the author of the node is not set, set it with the default author name of
        // the new ML Container
        String nodeAuthor = (String) nodeService.getProperty(actionedUponNodeRef, ContentModel.PROP_AUTHOR);
        if (nodeAuthor == null || nodeAuthor.length() < 1)
           nodeService.setProperty(actionedUponNodeRef, ContentModel.PROP_AUTHOR, mlAuthor);
		NodeRef mlContainer = multilingualContentService.getTranslationContainer(actionedUponNodeRef);
		
		nodeService.setProperty(mlContainer, ContentModel.PROP_AUTHOR, mlAuthor);

    }
    
}
