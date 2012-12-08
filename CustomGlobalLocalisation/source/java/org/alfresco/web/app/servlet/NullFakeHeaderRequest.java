package org.alfresco.web.app.servlet;
import java.util.ArrayList;
  import java.util.Collections;
  import java.util.Enumeration;
  import java.util.List;
import java.util.StringTokenizer;

  import javax.servlet.http.Cookie;
  import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Fake header request, make it null so the WebscriptServlet 
 * will not touch values set in LanguageCookieFilter
 * @author philippe
 *
 */
  public class NullFakeHeaderRequest extends HttpServletRequestWrapper {

	  private String lang;
      /**
       * Constructor.
       *
       * @param request HttpServletRequest.
       */
      public NullFakeHeaderRequest(HttpServletRequest request, String lang) {
          super(request);
          this.lang = lang;
      }

      public String getHeader(String name) {
          HttpServletRequest request = (HttpServletRequest)getRequest();

          //if we are looking for the "Accept-Language" request header
          if("Accept-Language".equals(name)) {
               return lang;
          }
          

          //otherwise fall through to wrapped request object
          return request.getHeader(name);
      }

 

  }