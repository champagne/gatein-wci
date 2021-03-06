/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.gatein.wci.jetty;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.gatein.wci.command.CommandServlet;
import org.gatein.wci.spi.WebAppContext;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author <a href="hoang281283@gmail.com">Minh Hoang TO</a>
 * @date 9/24/12
 */
public class Jetty8WebAppContext implements WebAppContext
{
   private static final String GATEIN_SERVLET_NAME = "CommandServlet"; //TODO: why is Jetty the only one calling this 'CommandServlet'?
   private static final String GATEIN_SERVLET_PATH = "/jetty8gateinservlet";

   private ServletHolder commandServlet;

   private final ServletContextHandler contextHandler;

   private ServletContext servletContext;

   private String contextPath;

   private ClassLoader classLoader;

   public Jetty8WebAppContext(ServletContextHandler contextHandler)
   {
      this.contextHandler = contextHandler;
      this.servletContext = contextHandler.getServletContext();
      this.contextPath = contextHandler.getContextPath();
      this.classLoader = contextHandler.getClassLoader();
   }

   @Override
   public void start() throws Exception
   {
      try
      {
         commandServlet = new ServletHolder();
         commandServlet.setName(GATEIN_SERVLET_NAME);
         commandServlet.setInitOrder(0);
         commandServlet.setClassName(CommandServlet.class.getName());
         contextHandler.addServlet(commandServlet, GATEIN_SERVLET_PATH);
      }
      catch (Exception ex)
      {
         clean();
      }
   }

   @Override
   public void stop()
   {
      clean();
   }

   private void clean()
   {
      if (commandServlet != null)
      {
         try
         {
            commandServlet.stop();
         }
         catch (Exception ex)
         {
         }
      }
   }

   @Override
   public ServletContext getServletContext()
   {
      return servletContext;
   }

   @Override
   public ClassLoader getClassLoader()
   {
      return classLoader;
   }

   @Override
   public String getContextPath()
   {
      return contextPath;
   }

   @Override
   public boolean importFile(String parentDirRelativePath, String name, InputStream source, boolean overwrite) throws IOException
   {
      return false;
   }

   @Override
   public HttpSession getHttpSession(String sessId)
   {
      return contextHandler.getSessionHandler().getSessionManager().getHttpSession(sessId);
   }

   @Override
   public void fireRequestDestroyed(ServletRequest servletRequest)
   {
      //Do Nothing
   }

   @Override
   public void fireRequestInitialized(ServletRequest servletRequest)
   {
      //Do Nothing
   }
}
