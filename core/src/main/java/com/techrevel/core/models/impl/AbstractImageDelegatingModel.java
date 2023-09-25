package com.techrevel.core.models.impl;

import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.commons.DownloadResource;
import com.day.cq.wcm.api.components.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class AbstractImageDelegatingModel extends AbstractComponentImpl {

    /**
     * Component property name that indicates which Image Component will perform the image rendering for composed components. When
     * rendering images, the composed components that provide this property will be able to retrieve the content policy defined for the
     * Image Component's resource type.
     */
    public static final String IMAGE_DELEGATE = "imageDelegate";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImageDelegatingModel.class);

    @ScriptVariable
    private Component component;

    private Resource toBeWrappedResource;
    private Resource imageResource;

    protected void setImageResource(@NotNull Component component, @NotNull Resource toBeWrappedResource) {
        this.toBeWrappedResource = toBeWrappedResource;
        this.component = component;
    }

    @JsonIgnore
    public Resource getImageResource() {
        if (imageResource == null && component != null) {
            String delegateResourceType = component.getProperties().get(IMAGE_DELEGATE, String.class);
            if (StringUtils.isEmpty(delegateResourceType)) {
                LOGGER.error("In order for image rendering delegation to work correctly you need to set up the imageDelegate property on" +
                        " the {} component; its value has to point to the resource type of an image component.", component.getPath());
            } else {
                imageResource = new ImageResourceWrapper(toBeWrappedResource, delegateResourceType);
            }
        }
        return imageResource;
    }


    /**
     * Check if the teaser has an image.
     *
     * The teaser has an image if the `{@value DownloadResource#PN_REFERENCE}` property is set and the value
     * resolves to a resource; or if the `{@value DownloadResource#NN_FILE} child resource exists.
     *
     * @return True if the teaser has an image, false if it does not.
     */
    protected boolean hasImage() {
        return Optional.ofNullable(this.resource.getValueMap().get(DownloadResource.PN_REFERENCE, String.class))
                .map(request.getResourceResolver()::getResource)
                .orElseGet(() -> request.getResource().getChild(DownloadResource.NN_FILE)) != null;
    }

    protected void initImage() {
        if (this.hasImage()) {
            this.setImageResource(component, request.getResource());
        }
    }
}

