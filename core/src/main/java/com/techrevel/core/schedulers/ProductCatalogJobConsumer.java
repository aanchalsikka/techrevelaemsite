package com.techrevel.core.schedulers;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, immediate = true, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + "product/catalog/generate"
})

public class ProductCatalogJobConsumer implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public JobResult process(final Job job) {
        // Retrieve product catalog details and rendering criteria
        String catalogPath = (String) job.getProperty("catalogPath");
        String criteria = (String) job.getProperty("criteria");
        logger.info("Catalog Path: {}", catalogPath);
        logger.info("Criteria: {}", criteria);

        // Logic for generating the product catalog based on criteria
        // Publish the catalog to the website
        return JobResult.OK;
    }
}