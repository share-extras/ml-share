(function()
{
   var $html = Alfresco.util.encodeHTML,
      $isValueSet = Alfresco.util.isValueSet;

   if (Alfresco.DocumentList)
   {
      YAHOO.Bubbling.fire("registerRenderer",
      {
         propertyName: "sys_locale",
         renderer: function sys_locale_renderer(record, label)
         {
            var jsNode = record.jsNode,
               properties = jsNode.properties,
               id = Alfresco.util.generateDomId(),
               html = "",
               localized;
            if (jsNode.hasAspect("sys:localized"))
            {
 			   localized = properties["sys:locale"];
            }
            return '<span id="' + id + '" class="item">Multiligual:' + localized.value + '</span>';
         }
      });
   }
})();
