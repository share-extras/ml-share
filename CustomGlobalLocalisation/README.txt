- place the jar in shared/lib
- cp -p custom-3x-slingshot-login.ftl WEB-INF/classes/share/templates/org/alfresco/global/slingshot-login.ftl
    (replace 3x by 33 or 34 as appropriate)
- add the following definition in <tomcat-home>/webapps/share/WEB-INF/web.xml
   <filter>
      <filter-name>Accept Language</filter-name>
     <filter-class>org.alfresco.web.app.servlet.FakeHeadersFilter</filter-class>
   </filter>

   <!-- map the filter to a URL pattern -->
   <filter-mapping>
     <filter-name>Accept Language</filter-name>
     <url-pattern>/*</url-pattern>
   </filter-mapping>
----------------------------------------
   <filter>
      <filter-name>ML content filter</filter-name>
      <description>Set the language for the content</description>
      <filter-class>org.alfresco.module.multibackend.SetMlLanguagePref</filter-class>
   </filter>

   <filter-mapping>
      <filter-name>ML content filter</filter-name>
      <url-pattern>/*</url-pattern>
   </filter-mapping>




Note : since there is no dedicated english language pack, for the selection of english to work the JVM default locale
must be set to english, i.e add :

export JAVA_OPTS=" ${JAVA_OPTS} -Duser.language=en -Duser.country=US"

For other languages or if your system is in English this is not required.