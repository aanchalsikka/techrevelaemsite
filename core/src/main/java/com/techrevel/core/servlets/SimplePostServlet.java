package com.techrevel.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

/**
 * A simple AEM servlet that handles POST requests.
 *
 * <p>This servlet is registered using a specific path (/bin/simplePostServlet)
 * and is capable of handling HTTP POST method. When a POST request is made
 * with parameters 'name' and 'email', the servlet returns a plain text response
 * echoing back those values.</p>
 *
 * <p>Note: This is a path-based servlet (not resourceType or selector-based),
 * and is generally suitable for internal utilities, form submissions, or
 * backend services where security and access control are tightly managed.</p>
 */
@Component(service = {Servlet.class}, property = {
        "sling.servlet.paths=" + SimplePostServlet.SERVLET_PATH,
        "sling.servlet.methods=POST"
})
public class SimplePostServlet extends SlingAllMethodsServlet {

    /** Logger instance for logging useful information or debugging */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** The servlet path used to invoke this servlet */
    public static final String SERVLET_PATH = "/bin/simplePostServlet";

    /**
     * Handles HTTP POST requests sent to /bin/simplePostServlet.
     *
     * @param request  the incoming Sling HTTP request
     * @param response the outgoing Sling HTTP response
     * @throws IOException in case of input/output issues while writing response
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        logger.info("Processing POST request in SimplePostServlet");

        // Set the response content type to plain text
        response.setContentType("text/plain");

        // Extract request parameters 'name' and 'email'
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        // Write response content showing received parameters
        response.getWriter().write("name = " + name + " & email = " + email);
    }
}
