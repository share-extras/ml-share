package org.alfresco.repo.activities.feed;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.dictionary.RepositoryLocation;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.preference.PreferenceService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.surf.util.I18NUtil;

public class EmailUserNotifierUserLanguage extends EmailUserNotifier {

	private static final String PREF_INTERFACELANGUAGE = "interface-language";
	private static final QName USER_LOCALE = QName.createQName(
			NamespaceService.SYSTEM_MODEL_1_0_URI, "locale");

	PreferenceService preferenceService;
	private RepositoryLocation feedEmailTemplateLocation;
	private SearchService searchService;
	private FileFolderService fileFolderService;
	
	
	public void setFileFolderService(FileFolderService fileFolderService) {
		this.fileFolderService = fileFolderService;
	}


	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}


	public void setFeedEmailTemplateLocation(
			RepositoryLocation feedEmailTemplateLocation) {
		this.feedEmailTemplateLocation = feedEmailTemplateLocation;
	}

	public void setPreferenceService(PreferenceService preferenceService) {
		this.preferenceService = preferenceService;
	}

	
    private NodeRef getEmailTemplateRef()
    {
        StoreRef store = feedEmailTemplateLocation.getStoreRef();
        String xpath = feedEmailTemplateLocation.getPath();
        
        if (! feedEmailTemplateLocation.getQueryLanguage().equals(SearchService.LANGUAGE_XPATH))
        {
            logger.warn("Cannot find the activities email template - repository location query language is not 'xpath': "+feedEmailTemplateLocation.getQueryLanguage());
            return null;
        }
        
        List<NodeRef> nodeRefs = searchService.selectNodes(nodeService.getRootNode(store), xpath, null, namespaceService, false);
        if (nodeRefs.size() != 1)
        {
            logger.warn("Cannot find the activities email template: "+xpath);
            return null;
        }

        return fileFolderService.getLocalizedSibling(nodeRefs.get(0));
    }

	protected void notifyUser(NodeRef personNodeRef, String subjectText,
			Map<String, Object> model, NodeRef templateNodeRef) {

		Map<QName, Serializable> personProps = nodeService
				.getProperties(personNodeRef);

		String userName = (String) personProps.get(ContentModel.PROP_USERNAME);
		Locale userLanguage = (Locale) personProps.get(USER_LOCALE);

		Locale savedLocale = I18NUtil.getLocale();

		I18NUtil.setLocale(userLanguage);
		try {
			//get the template in the appropriate language
			NodeRef langTemplateNodeRef = getEmailTemplateRef();
			super.notifyUser(personNodeRef, subjectText, model, langTemplateNodeRef);
		} finally {
			// restore original
			I18NUtil.setLocale(savedLocale);
		}
	}

}
