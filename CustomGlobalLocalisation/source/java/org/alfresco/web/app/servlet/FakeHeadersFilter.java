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


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;



  /**
   * A simple filter used to create an additional header.
   *
   * @author Jee Vang
   *
   */
  public class FakeHeadersFilter implements Filter {

      public void destroy() {

      }

      public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
              FilterChain filterChain) throws IOException, ServletException {
          //if the ServletRequest is an instance of HttpServletRequest
          if(servletRequest instanceof HttpServletRequest) {
              //cast the object
              HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
              //create the FakeHeadersRequest object to wrap the HttpServletRequest
              FakeHeadersRequest request = new FakeHeadersRequest(httpServletRequest);
              //continue on in the filter chain with the FakeHeaderRequest and ServletResponse objects
              filterChain.doFilter(request, servletResponse);
          } else {
              //otherwise, continue on in the chain with the ServletRequest and ServletResponse objects
              filterChain.doFilter(servletRequest, servletResponse);
          }

          return;
      }

      public void init(FilterConfig filterConfig) throws ServletException {

      }

  }