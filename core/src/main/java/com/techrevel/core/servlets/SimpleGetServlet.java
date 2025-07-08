package com.techrevel.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * A simple example of an AEM Sling GET servlet that responds with the `jcr:title`
 * property of the requested resource.
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes = "weretail/components/structure/page",  // Applies to pages with this resource type
        methods = HttpConstants.METHOD_GET,                    // Handles GET requests
        extensions = "txt"                                     // Responds to .txt extension
)
@ServiceDescription("Simple Demo Servlet")
public class SimpleGetServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Handles HTTP GET requests.
     *
     * Responds with the value of the 'jcr:title' property of the requested resource.
     * The response content type is set to 'text/plain'.
     */
    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        logger.info("Inside SimpleGetServlet doGet method");

        // Retrieve the resource targeted by the current request
        final Resource resource = req.getResource();

        // Set response content type
        resp.setContentType("text/plain");

        // Write the jcr:title property (if available) to the response
        String title = resource.getValueMap().get(JcrConstants.JCR_TITLE, String.class);
        resp.getWriter().write("Title = " + (title != null ? title : "No title found"));
    }
}
