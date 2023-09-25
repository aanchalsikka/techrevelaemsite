package com.techrevel.core.models;

import com.adobe.cq.wcm.core.components.models.Component;
import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface Feature extends Component {
    default String getTitle() {
        return null;
    }

    default Resource getImageResource() {
        return null;
    }
}
