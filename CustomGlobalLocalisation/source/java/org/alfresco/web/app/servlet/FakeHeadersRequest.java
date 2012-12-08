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
   * Fake headers request object. Adds a request header
   * with the name "username". The value of this request header
   * will be taken from a cookie (also with the name, "username").
   *
   * @author Jee Vang
   *
   */
  public class FakeHeadersRequest extends HttpServletRequestWrapper {

      /**
       * Constructor.
       *
       * @param request HttpServletRequest.
       */
      public FakeHeadersRequest(HttpServletRequest request) {
          super(request);
      }

      public String getHeader(String name) {
          //get the request object and cast it
          //Accept-Language: fr-fr,fr;q=0.5
          //ALFRESCO_UI_PREFLANG=ja_JP
          HttpServletRequest request = (HttpServletRequest)getRequest();

          //if we are looking for the "Accept-Language" request header
          if("Accept-Language".equals(name)) {
              //loop through the cookies
              Cookie[] cookies = request.getCookies();

              //if cookies are null, then return null
              if(null == cookies) {
                  return null;
              }

              for(int i=0; i < cookies.length; i++) {
                  //if the cookie's name is "ALFRESCO_UI_PREFLANG"
                  if("ALFRESCO_UI_PREFLANG".equals(cookies[i].getName())) {
                     //get its value and return it
                      String val = cookies[i].getValue();
                      return val;
                  }
              }
          }

          //otherwise fall through to wrapped request object
          return request.getHeader(name);
      }

      public Enumeration getHeaderNames() {
          //loop over request headers from wrapped request object
          HttpServletRequest request = (HttpServletRequest)getRequest();
          return request.getHeaderNames();
      }

  }