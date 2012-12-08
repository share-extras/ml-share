package org.alfresco.web.evaluator;

import org.alfresco.error.AlfrescoRuntimeException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class IsPivotLanguage extends BaseEvaluator {


	public boolean evaluate(JSONObject jsonObject) {
	      try
	        {
	    	   JSONObject decoratedLocale = (JSONObject)this.getProperty(jsonObject, "sys:locale");
	    	   if (decoratedLocale == null)
	    		   return false;
	    	   Object isPivot = decoratedLocale.get("ispivotlanguage");
	    	   
	           if(isPivot == null) 
	        	   return false;
	    	   Boolean isPivotBoolean = (Boolean)isPivot;
	           if(isPivotBoolean.equals(Boolean.TRUE))
	        	   return true;
	           else
	        	   return false;      
	        }
	        catch (Exception err)
	        {
	            throw new AlfrescoRuntimeException("Failed to run action IsPivotLanguage: " + err.getMessage());
	        }

	    }


}
