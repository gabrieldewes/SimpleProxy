package com.dewes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by Dewes on 15-nov-2017
 */
@SuppressWarnings("serial")
public class ForwardServlet extends HttpServlet {

    private static final String TARGET_BASE_URL = "http://127.0.0.1";

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        forwardRequest("GET", httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        forwardRequest("POST", httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        forwardRequest("PUT", httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doDelete(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        forwardRequest("DELETE", httpServletRequest, httpServletResponse);
    }

    private void forwardRequest(String method, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        final boolean hasOutBody = ( method.equals("POST") || method.equals("PUT") );

        try {
            final URL url = new URL(TARGET_BASE_URL
                    + httpServletRequest.getRequestURI()
                    + (httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : ""));

            HttpURLConnection targetHttpConnection = (HttpURLConnection) url.openConnection();

            targetHttpConnection.setRequestMethod(method);

            final Enumeration headers = httpServletRequest.getHeaderNames();

            while (headers.hasMoreElements()) {
                final String header = (String) headers.nextElement();
                final Enumeration values = httpServletRequest.getHeaders(header);

                while (values.hasMoreElements()) {
                    final String value = (String) values.nextElement();
                    targetHttpConnection.addRequestProperty(header, value);
                }
            }

            // targetHttpConnection.setFollowRedirects(false);
            targetHttpConnection.setUseCaches(false);
            targetHttpConnection.setDoInput(true);
            targetHttpConnection.setDoOutput(hasOutBody);

            final byte[] buffer = new byte[16384];

            while (hasOutBody) {
                final int read = httpServletRequest.getInputStream().read(buffer);

                if (read <= 0) break;

                targetHttpConnection.getOutputStream().write(buffer, 0, read);
            }

            httpServletResponse.setStatus(targetHttpConnection.getResponseCode());

            for (int i=0; ; ++i) {
                final String header = targetHttpConnection.getHeaderFieldKey(i);

                if (header == null) break;

                final String value = targetHttpConnection.getHeaderField(i);
                httpServletResponse.setHeader(header, value);
            }

            while (true) {
                final int read = targetHttpConnection.getInputStream().read(buffer);

                if (read <= 0) break;

                httpServletResponse.getOutputStream().write(buffer, 0, read);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
