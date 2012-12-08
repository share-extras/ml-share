<#import "/org/alfresco/repository/forms/pickerresults.lib.ftl" as pickerResultsLib />

<#macro pickerResultsTranslationJSON results>
    <#escape x as jsonUtils.encodeJSONString(x)>
{
    "data":
    {
<#if parent??>
    <@pickerResultsLib.renderParent parent />
</#if>
        "items":
        [
        <#list results as row>
            {
                "type": "${row.item.typeShort}",
                "parentType": "${row.item.parentTypeShort!""}",
                "isContainer": ${row.item.isContainer?string},
                "name": "${row.item.properties.name!""}",
                "title": "${row.item.properties.title!""}",
                "description": "${row.item.properties.description!""}",
                <#if row.item.properties.modified??>"modified": "${xmldate(row.item.properties.modified)}",</#if>
                <#if row.item.properties.modifier??>"modifier": "${row.item.properties.modifier}",</#if>
                <#if row.item.siteShortName??>"site": "${row.item.siteShortName}",</#if>
                "displayPath": "${row.item.displayPath!""}",
                "nodeRef": "${row.item.nodeRef}"<#if row.selectable?exists>,
                "selectable" : ${row.selectable?string}</#if>,
                "language" : "en"
            }<#if row_has_next>,</#if>
        </#list>
        ]
    }
}
    </#escape>
</#macro>


<@pickerResultsTranslationJSON results=results />