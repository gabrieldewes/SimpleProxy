package com.dewes;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Created by Dewes on 15-nov-2017
 */
public class MainEndpoint {

    public static void main(String[] args) {

        try {
            ServletContextHandler servletContextHandler = new ServletContextHandler();
            servletContextHandler.setContextPath("/");
            servletContextHandler.addServlet(ForwardServlet.class, "/*");

            FilterHolder filterHolder = servletContextHandler
                    .addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

            filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
            filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
            filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "*");
            filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "*");


            // Server server = new Server(9000);
            // Server server = new Server(Integer.valueOf(System.getProperty("PORT", "8080")));
            Server server = new Server(Integer.valueOf(System.getenv("PORT")));
            server.setHandler(servletContextHandler);

            server.start();
            server.join();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
