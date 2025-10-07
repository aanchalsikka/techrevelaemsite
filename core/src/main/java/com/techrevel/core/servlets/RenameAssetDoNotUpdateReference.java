package com.techrevel.core.servlets;

import com.adobe.granite.asset.api.AssetManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

/**
 * Servlet to rename an AEM asset, but it does not update its references programmatically.
 */
@Component(
        service = {Servlet.class},
        property = {
                "sling.servlet.paths=/bin/asset-rename-no-reference-update",
                "sling.servlet.methods=GET"
        }
)
public class RenameAssetDoNotUpdateReference extends SlingSafeMethodsServlet {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        String assetPath = "/content/dam/techrevel/skitouring.jpg";
        String newName = "skitouring-renamed.jpg";

        ResourceResolver resolver = request.getResourceResolver();
        AssetManager assetManager = resolver.adaptTo(AssetManager.class);

        String parentPath = assetPath.substring(0, assetPath.lastIndexOf('/'));
        String extension = assetPath.substring(assetPath.lastIndexOf('.'));
        String newAssetPath = parentPath + "/" + newName + extension;

        try {
            assetManager.moveAsset(assetPath, newAssetPath);
            resolver.commit();
            response.getWriter().write("Renamed asset to: " + newAssetPath);
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
