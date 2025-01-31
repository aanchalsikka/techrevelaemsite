package com.techrevel.core.workflows;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Custom Workflow Process Step to Extract Clipping Path Name
 */
@Component(
        service = WorkflowProcess.class,
        property = {"process.label=Extract Clipping Path Metadata"}
)
public class ClippingPathExtractorProcess implements WorkflowProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ClippingPathExtractorProcess.class);
    private static final String AEM_METADATA_PROPERTY = "dam:ClippingPathName";

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) {
        String payloadPath = WorkflowUtils.getPayloadPath(workItem);

        try {
            // Get the ResourceResolver from workflow session
            ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);
            Resource assetResource = resourceResolver.getResource(payloadPath);

            if (assetResource == null) {
                LOG.error("Asset not found at: {}", payloadPath);
                return;
            }

            Asset asset = assetResource.adaptTo(Asset.class);
            if (asset == null) {
                LOG.error("Failed to adapt resource to asset: {}", payloadPath);
                return;
            }

            try (InputStream is = asset.getRendition("original").getStream()) {
                Metadata metadata = ImageMetadataReader.readMetadata(is);
                PhotoshopDirectory photoshopDir = metadata.getFirstDirectoryOfType(PhotoshopDirectory.class);

                if (photoshopDir != null) {
                    String clippingPathName = photoshopDir.getDescription(PhotoshopDirectory.TAG_CLIPPING_PATH_NAME);

                    if (clippingPathName != null) {
                        Resource metadataRes = assetResource.getChild("jcr:content/metadata");
                        ModifiableValueMap map = metadataRes.adaptTo(ModifiableValueMap.class);
                        map.put(AEM_METADATA_PROPERTY, clippingPathName);
                        LOG.info("Stored Clipping Path Name: {}", clippingPathName);
                    } else {
                        LOG.warn("No Clipping Path Name found in asset: {}", payloadPath);
                    }
                }
            } catch (Exception e) {
                LOG.error("Error reading Photoshop metadata for asset: {}", payloadPath, e);
            }
        } catch (Exception e) {
            LOG.error("Unable to complete processing the Workflow Process step", e);
        }
    }
}
