package com.techrevel.core.models.impl;

import com.techrevel.core.models.Feature;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Feature.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FeatureImpl extends AbstractImageDelegatingModel implements Feature {

    @ValueMapValue(name = "jcr:title")
    private String title;

    @PostConstruct
    private void init() {
        initImage();
    }

    public String getTitle() {
        return title;
    }
}
