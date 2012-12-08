    //  Search for an element to place the Menu Button into via the 
    //  Event utility's "onContentReady" method
 var oMenuButton5;
    YAHOO.util.Event.onContentReady("menubuttonsfromjavascript", function () {

        var me = this;



        //  "click" event handler for each item in the Button's menu

        var onMenuItemClick = function (p_sType, p_aArgs, p_oItem) {
            
            //var sText = p_oItem.cfg.getProperty("text");
            //var selectedLang = oMenuButton5.get("label");
	    var selectedLang= p_oItem.value;
            YAHOO.log("[MenuItem Properties] text: " + selectedLang + ", value: " + 
                    p_oItem.value);
            
            // call api to remove all items from the trashcan
            // use the progress animation as this operation may take a while
            var progressPopup = Alfresco.util.PopupManager.displayMessage(
            {
             displayTime: 0,
             effect: null,
             text: "In progress..."
            });
          
            Alfresco.util.Ajax.request(
            {
              url: Alfresco.constants.PROXY_URI + "/multilingual/setmllanguagepref?mllang=" + selectedLang,
              method: "GET",
              successCallback:
              {
                 fn: function success(data)
                 {
                     progressPopup.destroy();
                     YAHOO.Bubbling.fire("metadataRefresh");
                 }
              },
              failureCallback:
              {
                 fn: function failure(data)
                 {
                    progressPopup.destroy();
                 }
              }
            });
            //get vlue from cookie MLLANG
            
            oMenuButton5.set("label", selectedLang);  
            YAHOO.util.Cookie.set("MLLANG", selectedLang);
            // Fire metadataRefresh so other components may update themselves
            //YAHOO.Bubbling.fire("metadataRefresh");
            //location.reload(true);
         

        };


        //  Create an array of YAHOO.widget.MenuItem configuration properties

        var aMenuButton5Menu = [

            { text: "en", value: "en", onclick: { fn: onMenuItemClick } },
            { text: "fr", value: "fr", onclick: { fn: onMenuItemClick } },
            { text: "nl", value: "nl", onclick: { fn: onMenuItemClick } },
            { text: "de", value: "de", onclick: { fn: onMenuItemClick } }
        ];


        //  Instantiate a Menu Button using the array of YAHOO.widget.MenuItem 
        //  configuration properties as the value for the "menu"  
        //  configuration attribute.

        oMenuButton5 = new YAHOO.widget.Button({    type: "menu", 
                                                        label: "en", 
                                                        name: "mymenubutton", 
                                                        menu: aMenuButton5Menu, 
                                                        container: this });
        if (YAHOO.util.Cookie === null)
        {
          var YC = YAHOO.util.Cookie;
        }
        
        var lang = YAHOO.util.Cookie.get("MLLANG", null);
        if (lang !== null)
        {
         oMenuButton5.set("label", lang);
        }
        else
        {
          oMenuButton5.set("label", "en");
          YAHOO.util.Cookie.set("MLLANG", "en");
        }           

    });

   // YAHOO.util.Event.onContentReady("menubuttonsfromjavascript", function () {
   //
   // });


    YAHOO.util.Event.onContentReady("ml-checkbox", function () {

        var me = this;

	var oElement = document.getElementById("ml-checkbox");
	

	function fnCallback(e) { 
		//position cookie
	        console.debug(e);
		//save state in cookie
		if(oElement.checked == true)
                {		
		   YAHOO.util.Cookie.set("FILTERSTATE", "true");
		}
                else
		{
		   YAHOO.util.Cookie.set("FILTERSTATE", "false"); 
		}
		
		var progressPopup = Alfresco.util.PopupManager.displayMessage(
            	{
             		displayTime: 0,
             		effect: null,
             		text: "In progress..."
            	});
		//get the language from the state of the menu		
		var selectedLang = oMenuButton5.get("label");
		var selectLangContent = selectedLang;
		if(oElement.checked == false)
		{
	           selectLangContent = "reset"; //no filter
		}
	
		Alfresco.util.Ajax.request(
		    {
		      url: Alfresco.constants.PROXY_URI + "/multilingual/setmllanguagepref?mllang=" + selectedLang +"&mllangcontent="+selectLangContent,
		      method: "GET",
		      successCallback:
		      {
		         fn: function success(data)
		         {
		             progressPopup.destroy();
			     YAHOO.Bubbling.fire("metadataRefresh");
		         }
		      },
		      failureCallback:
		      {
		         fn: function failure(data)
		         {
		            progressPopup.destroy();
		         }
		      }
		    });
		//location.reload(true);
		
		
		
        }

        var filterState = YAHOO.util.Cookie.get("FILTERSTATE", null);
        if (filterState === null || filterState == "false")
        {
           oElement.checked=false;
        }
        else
        {
 	   oElement.checked=true;
        }           

	YAHOO.util.Event.addListener(oElement, "click", fnCallback);


  
    });
