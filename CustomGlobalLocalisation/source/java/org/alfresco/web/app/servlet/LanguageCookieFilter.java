/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.web.app.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.extensions.surf.util.I18NUtil;

/**
 * A simple filter used to get ML language pref from param "mllang" and set
 * cookie "MLPREF" with the corresponding value If no param "mllang" set content
 * locale from "MLPREF" cookie.
 * 
 * @author Ph Dubois
 * 
 */
public class LanguageCookieFilter implements Filter {

	static private final String MLPREF = "MLPREF";
	static private final String MLPREFCONTENT = "MLPREFFILTER";

	public void destroy() {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		String lang = null;
		// check if parameter "mllang" is there
		String mlLang = servletRequest.getParameter("mllang");
		String mlLangContent = servletRequest.getParameter("mllangcontent");
		dumpCookies(servletRequest);
		String locale = positionCookie(servletRequest, servletResponse, mlLang, MLPREF, false);
		
		if(mlLangContent !=null && mlLangContent.equals("reset"))
		{
			Cookie mlpref = new Cookie(MLPREFCONTENT, ""); // Set expiry date after 24 Hrs forboth the cookies. 
			mlpref.setMaxAge(0); 
			((HttpServletResponse) servletResponse).addCookie(mlpref);
		    I18NUtil.setContentLocale(null);
		}
		else
		{
		    positionCookie(servletRequest, servletResponse, mlLangContent, MLPREFCONTENT, true);
		}
		// create the NullFakeHeaderRequest so WebscriptServlet does not modify
		// the request again.
		// recuperate the lang
		if (locale == null) {
			// default it to "en"
			locale = "en";
		}
		NullFakeHeaderRequest request = new NullFakeHeaderRequest((HttpServletRequest) servletRequest, locale);
		filterChain.doFilter(request, servletResponse);

		return;
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	protected void dumpCookies(ServletRequest servletRequest)
	{
		Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
		if (cookies == null)
			return;
		Cookie cookie;
		for (int i = 0; i < cookies.length; i++) {
			cookie = cookies[i];
			System.out.println("Cookie Name: " + cookie.getName() + " Value:*" + cookie.getValue() + "*");
			}
	}
	
	protected String positionCookie(ServletRequest servletRequest, ServletResponse servletResponse, String mlLang,
			String cookieName, boolean content) {
		if (mlLang == null || mlLang.length() == 0) {
			// try to get the cookie and set the content locale prop there
			Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
			Cookie cookie;
			boolean found = false;
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					cookie = cookies[i];
					if (cookie.getName().equals(cookieName)) {
						String lang = cookie.getValue();
						if (content) {
							if(lang.length()== 0)
							{
							   I18NUtil.setContentLocale(null);
							}
							else
							{
							   I18NUtil.setContentLocale(new Locale(lang));	
							}
							return lang;
						} else {
							I18NUtil.setLocale(new Locale(lang));
							return lang;
						}

					}
				}
			}
		} else {
			Cookie mlpref = getCookie(servletRequest, cookieName);
			
			if( mlpref == null)
			{
			   mlpref = new Cookie(cookieName, mlLang);
			}
			else
				mlpref.setValue(mlLang);
			// Set expiry date after 24 Hrs for both the cookies.
			//mlpref.setMaxAge(60 * 60 * 24);

			((HttpServletResponse) servletResponse).addCookie(mlpref);
			if (content) {
				I18NUtil.setContentLocale(new Locale(mlLang));
				return mlLang;
			} else {
				I18NUtil.setLocale(new Locale(mlLang));
				return mlLang;
			}
			/*
			 * { if(mlLang.equals("reset")) { Cookie mlpref = new
			 * Cookie(cookieName, mlLang); // Set expiry date after 24 Hrs for
			 * both the cookies. mlpref.setMaxAge(0); mlpref.setValue("");
			 * ((HttpServletResponse) servletResponse).addCookie(mlpref); if
			 * (content) { I18NUtil.setContentLocale(null); return mlLang; }
			 * else { I18NUtil.setLocale(null); return mlLang; } } else
			 */

		}

		return mlLang;
	}
	
    public static Cookie getCookie(ServletRequest request, String name) {
        if (((HttpServletRequest) request).getCookies() != null) {
            for (Cookie cookie : ((HttpServletRequest) request).getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }
}
