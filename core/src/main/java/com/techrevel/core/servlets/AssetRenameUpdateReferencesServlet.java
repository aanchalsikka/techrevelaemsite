package com.techrevel.core.servlets;

import com.day.cq.dam.api.AssetReferenceResolver;
import com.day.cq.wcm.command.api.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to rename an AEM asset and update its references programmatically.
 * This servlet uses the CopyMoveCommandBuilder API to move (rename) the asset in the DAM.
 * References to the asset in content are updated automatically by the command.
 */
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/asset-rename-update-references",
                "sling.servlet.methods=GET"
        }
)
public class AssetRenameUpdateReferencesServlet extends SlingSafeMethodsServlet {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private CommandBuilderFactory commandBuilderFactory;

    @Reference
    private AssetReferenceResolver assetReferenceResolver;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        // Source path of the asset to be renamed
        String srcPath = "/content/dam/techrevel/skitouring1.jpg";
        // Destination path with the new name for the asset
        String dstPath = "/content/dam/techrevel/skitouring1-rename.jpg";

        ResourceResolver resolver = request.getResourceResolver();

        try {
            CopyMoveCommandBuilder copyMoveCommandBuilder = commandBuilderFactory.createCommandBuilder(CopyMoveCommandBuilder.class)
                    .withCopy(false)
                    .withResourceResolver(resolver)
                    .withAssetReferenceResolver(assetReferenceResolver)
                    .withRetrieveAllRefs(true)
                    .withShallow(false)
                    .withCheckIntegrity(true);

            // Build the argument specifying source, destination paths and reference adjustment option
            CopyMoveCommandPathArgument copyMoveCmdPathArg = (CopyMoveCommandPathArgument) copyMoveCommandBuilder.createPathArgumentBuilder()
                    .withSrcPath(srcPath)
                    .withDstPath(dstPath)
                    .withAdjustRefPaths(null)
                    .build();

            copyMoveCommandBuilder.withPathArgument(copyMoveCmdPathArg);

            Command cmd = copyMoveCommandBuilder.build();

            // Execute the rename command
            CopyMoveCommandResult result = (CopyMoveCommandResult) cmd.execute();

            if (result.executionSucceeded()) {
                response.getWriter().write("Asset renamed successfully from " + srcPath + " to " + dstPath);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to rename asset: " + result.getDestinationPaths());
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Exception: " + e.getMessage());
        }
    }
}