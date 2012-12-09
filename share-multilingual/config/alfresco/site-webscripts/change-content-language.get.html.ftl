<#include "org/alfresco/components/component.head.inc">
<@script type="text/javascript" src="${url.context}/res/yui/cookie/cookie.js"></@script>
<@script type="text/javascript" src="${url.context}/res/modules/multimodule/change-content-lang.js"></@script>

    <fieldset id="menubuttons">
        <fieldset id="menubuttonsfromjavascript">
            <legend>${msg("select.multilingual.language")}:</legend>
        </fieldset>       
    </fieldset>
    ${msg("change.content.lang.filter")}:<input id="ml-checkbox" type="checkbox" name="fileChecked"  >
 