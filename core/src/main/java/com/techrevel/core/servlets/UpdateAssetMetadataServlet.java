package com.techrevel.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                ServletResolverConstants.SLING_SERVLET_PATHS + "=" + "/bin/updateAssetMetadata"
        })
public class UpdateAssetMetadataServlet extends SlingSafeMethodsServlet {

    private static final String METADATA_PROPERTY = "jcr:title"; // Example metadata

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response) throws ServletException, IOException {
        String assetPath = "/content/dam/wknd/en/site/wknd-logo-dk.png";
        String newTitle = "Updated Title";

        ResourceResolver resolver = request.getResourceResolver();
        Session session = resolver.adaptTo(Session.class);

        try {
            // Set user data to prevent workflow launcher triggering
            session.getWorkspace().getObservationManager().setUserData("doNotTriggerRfAWorkflow");

            // Access the asset's metadata node
            Node metadataNode = session.getNode(assetPath + "/jcr:content/metadata");

            // Update the metadata property
            metadataNode.setProperty(METADATA_PROPERTY, newTitle);

            // Save JCR session
            session.save();

            response.getWriter().write("Metadata updated successfully without triggering workflow");
        } catch (RepositoryException e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating metadata: " + e.getMessage());
        }
    }
}
