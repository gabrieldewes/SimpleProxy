package com.dewes;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * Created by Dewes on 15-nov-2017
 */
public class MainEndpoint {

    public static void main(String[] args) {

        try {
            ServletHandler servletHandler = new ServletHandler();

            Server server = new Server(9000);
            // Server server = new Server(Integer.valueOf(System.getProperty("PORT", "8080")));
            // Server server = new Server(Integer.valueOf(System.getenv("PORT")));
            server.setHandler(servletHandler);

            servletHandler.addServletWithMapping(ForwardServlet.class, "/*");

            server.start();
            server.join();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
