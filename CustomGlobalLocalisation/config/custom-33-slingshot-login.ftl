<#include "../include/alfresco-template.ftl" />
<@templateHeader>
   <@link rel="stylesheet" type="text/css" href="${url.context}/templates/login/login.css" />
   <@link rel="stylesheet" type="text/css" href="${url.context}/themes/${theme}/login.css" />
</@>
<@templateBody>
   <div id="alflogin" class="login-panel">
      <div class="login-logo"></div>
      <form id="loginform" accept-charset="UTF-8" method="get" action="${url.context}/page/dologin" onsubmit="return alfLogin();">
         <fieldset>
            <div style="padding-top:96px">
               <label id="txt-username" for="username"></label>
            </div>
            <div style="padding-top:4px">
               <input type="text" id="username" name="username" maxlength="255" style="width:200px" value="<#if lastUsername??>${lastUsername?html}</#if>" />
            </div>
            <div style="padding-top:12px">
               <label id="txt-password" for="password"></label>
            </div>
            <div style="padding-top:4px">
               <input type="password" id="password" name="password" maxlength="255" style="width:200px"/>
            </div>
			<div style="padding-top:12px">
               <label id="txt-language" for="language"></label>
            </div>
            <div style="padding-top:4px">
               <select id="language" name="language" size="1" style="width:150px">
                  <option value="en-en,en;q=0.5" selected="selected">English</option>
                  <option value="de-de,de;q=0.5">German</option>
                  <option value="es-es,es;q=0.5">Spanish</option>
                  <option value="fr-fr,fr;q=0.5">French</option>
                  <option value="it-it,it;q=0.5">Italian</option>
                  <option value="ja-ja;ja;q=0.5">Japanese</option>
				</select>
            </div>
            <div style="padding-top:16px">
               <input type="submit" id="btn-login" class="login-button" />
            </div>
            <div style="padding-top:32px">
               <span class="login-copyright">
                  &copy; 2005-2010 Alfresco Software Inc. All rights reserved.
               </span>
            </div>
            <input type="hidden" id="success" name="success" value="${successUrl?html}"/>
            <input type="hidden" name="failure" value="<#assign link>${url.context}/page/type/login</#assign>${link?html}?error=true"/>
         </fieldset>
      </form>
   </div>

   <script type="text/javascript">//<![CDATA[

	function getCookie( name ) {//get cookie name
	  var start = document.cookie.indexOf( name + "=" ); //variable start
	  var len = start + name.length + 1; //variable len
	  if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {
		return null;//if vars do not exists
	  }
	  if ( start == -1 ) return null; //if var is negative
	  var end = document.cookie.indexOf( ";", len ); //end variable
	  if ( end == -1 ) end = document.cookie.length; //if end is negative
	  return unescape( document.cookie.substring( len, end ) ); //return cookie
	}


   function alfLogin()
   {
      var Dom = YAHOO.util.Dom;
      var today = new Date();
      var expires_date = new Date( today.getTime() + (24*60*60*1000*60) ); // cookie persist for 60 day
	  var value = Dom.get("language").value;
	  //document.cookie = "ALFRESCO_UI_PREFLANG"+"="+escape(value)+";path=/;expires=" + expires_date.toGMTString();
	  document.cookie = "ALFRESCO_UI_PREFLANG"+"="+escape(value)+";path=/";
      YAHOO.util.Dom.get("btn-login").setAttribute("disabled", true);
      return true;
   }

   YAHOO.util.Event.onContentReady("alflogin", function()
   {
      var Dom = YAHOO.util.Dom;
  	  //var today = new Date();
      //var expires_date = new Date( today.getTime() + (24*60*60*1000*60) ); // cookie persist for 60 day
	  //document.cookie = "ALFRESCO_UI_PREFLANG"+"="+escape("test valeur")+";path=/;expires=" + expires_date.toGMTString();

	  //var cookie_value = getCookie( "ALFRESCO_UI_PREFLANG" );

      // Prevent the Enter key from causing a double form submission
      var form = Dom.get("loginform");
      // add the event to the form and make the scope of the handler this form.
      YAHOO.util.Event.addListener(form, "submit", this._submitInvoked, this, true);
      var fnStopEvent = function(id, keyEvent)
      {
         if (form.getAttribute("alflogin") == null)
         {
            form.setAttribute("alflogin", true);
         }
      }

      var enterListener = new YAHOO.util.KeyListener(form,
      {
         keys: YAHOO.util.KeyListener.KEY.ENTER
      }, fnStopEvent, "keydown");
      enterListener.enable();

      // set I18N labels
      Dom.get("txt-username").innerHTML = Alfresco.util.message("label.username") + ":";
      Dom.get("txt-password").innerHTML = Alfresco.util.message("label.password") + ":";
	  Dom.get("txt-language").innerHTML = Alfresco.util.message("label.language");
      Dom.get("btn-login").value = Alfresco.util.message("button.login");

      // generate and display main login panel
      var panel = new YAHOO.widget.Overlay(YAHOO.util.Dom.get("alflogin"),
      {
         modal: false,
         draggable: false, // NOTE: Don't change to "true"
         fixedcenter: true,
         close: false,
         visible: true,
         iframe: false
      });
      panel.render(document.body);

      Dom.get("success").value += window.location.hash;
      Dom.get(<#if lastUsername??>"password"<#else>"username"</#if>).focus();
   });

<#if url.args["error"]??>
   Alfresco.util.PopupManager.displayPrompt(
   {
      title: Alfresco.util.message("message.loginfailure"),
      text: Alfresco.util.message("message.loginautherror"),
      buttons: [
      {
         text: Alfresco.util.message("button.ok"),
         handler: function error_onOk()
         {
            this.destroy();
            YAHOO.util.Dom.get("username").focus();
            YAHOO.util.Dom.get("username").select();
         },
         isDefault: true
      }]
   });
</#if>
   //]]></script>
</@>
</body>
</html>