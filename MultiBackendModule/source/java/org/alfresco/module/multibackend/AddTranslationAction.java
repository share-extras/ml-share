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

public class AddTranslationAction extends ActionExecuterAbstractBase {
	private static Log logger = LogFactory.getLog(MakeMultilingualAction.class);

	// Form parameters
	public static final String PARAM_SOURCE_TRANSLATION = "sourcetranslation";
	public static final String PARAM_SOURCE_TRANSLATION_ADDED = "sourcetranslation_added";
	public static final String PARAM_SOURCE_TRANSLATION_REMOVED = "sourcetranslation_removed";
	public static final String PARAM_TRANSLATION_LANGUAGE = "sourcetranslation_lang";

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
		paramList.add(new ParameterDefinitionImpl(PARAM_SOURCE_TRANSLATION,
				DataTypeDefinition.TEXT, false, ""));
		paramList.add(new ParameterDefinitionImpl(PARAM_SOURCE_TRANSLATION_ADDED,
				DataTypeDefinition.TEXT, false, ""));
		paramList.add(new ParameterDefinitionImpl(PARAM_SOURCE_TRANSLATION_REMOVED,
				DataTypeDefinition.TEXT, false, ""));
		paramList.add(new ParameterDefinitionImpl(PARAM_TRANSLATION_LANGUAGE,
				DataTypeDefinition.TEXT, false, ""));

	}

	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		if (logger.isDebugEnabled()) {
			logger.debug("Action upon NodeRef in AddTranslationAction:" + actionedUponNodeRef);
			logger.debug("Source translation: "
					+ action.getParameterValue(PARAM_SOURCE_TRANSLATION));
			logger.debug("Translation added: "
					+ action.getParameterValue(PARAM_SOURCE_TRANSLATION_ADDED));
			logger.debug("Tanslation removed: "
					+ action.getParameterValue(PARAM_SOURCE_TRANSLATION_REMOVED));
			logger.debug("Added language: " + action.getParameterValue(PARAM_TRANSLATION_LANGUAGE));
		}
		String addedLanguages = (String) action.getParameterValue(PARAM_TRANSLATION_LANGUAGE);

		String addedLanguagesSplit[] = addedLanguages.split(",");

		// get pivot nodeRef
		NodeRef pivotTranslation = multilingualContentService
				.getPivotTranslation(actionedUponNodeRef);

		for (int i = 0; i < addedLanguagesSplit.length; i++) {
			// do not touch pivot language
			String nodeRefAndLangSplit[] = addedLanguagesSplit[i].split("\\.");
			String nodeRefStr = nodeRefAndLangSplit[0];
			String lang = nodeRefAndLangSplit[1];
			// check of NodeRef is multilingual
			NodeRef nodeRefTranslation = new NodeRef(nodeRefStr);
			if (multilingualContentService.isTranslation(nodeRefTranslation)) {
				// check that it is in the same multilingual document
				if (multilingualContentService.getPivotTranslation(nodeRefTranslation).equals(
						pivotTranslation)) {
					// get language and modify it if required
					Locale loc = (Locale) nodeService.getProperty(nodeRefTranslation,
							ContentModel.PROP_LOCALE);
					if (!loc.getLanguage().endsWith(lang)) {
						// modify the locale
						nodeService.setProperty(nodeRefTranslation, ContentModel.PROP_LOCALE,
								new Locale(lang));
					}
				}
			} else {
				// add the node as a translation
				multilingualContentService.addTranslation(nodeRefTranslation, actionedUponNodeRef,
						new Locale(lang));
			}
		}
	}
}
