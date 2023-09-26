package com.techrevel.core.models.impl;

import com.adobe.cq.export.json.ExporterConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Exporter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME , extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ImageResourceWrapper extends ResourceWrapper {

    private ValueMap valueMap;
    private String resourceType;

    public ImageResourceWrapper(@NotNull Resource resource, @NotNull String resourceType) {
        super(resource);
        if (StringUtils.isEmpty(resourceType)) {
            throw new IllegalArgumentException("The " + ImageResourceWrapper.class.getName() + " needs to override the resource type of " +
                    "the wrapped resource, but the resourceType argument was null or empty.");
        }
        this.resourceType = resourceType;
        valueMap = new ValueMapDecorator(new HashMap<>(resource.getValueMap()));
        valueMap.put(ResourceResolver.PROPERTY_RESOURCE_TYPE, resourceType);
    }

    @Override
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        if (type == ValueMap.class) {
            return (AdapterType) valueMap;
        }
        return super.adaptTo(type);
    }

    @Override
    @NotNull
    public ValueMap getValueMap() {
        return valueMap;
    }

    @Override
    public String getResourceType() {
        return resourceType;
    }

    @Override
    public boolean isResourceType(String resourceType) {
        return this.getResourceResolver().isResourceType(this, resourceType);
    }
}
