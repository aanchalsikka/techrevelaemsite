package com.techrevel.core.schedulers;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, immediate = true,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + "content/archival/scheduled"
        })
public class ContentArchivalJobConsumer implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public JobResult process(Job job) {
        // Logic for processing content archival job
        // You can access job properties and execute the archival process here

        // Check for any custom parameters in the job
        String customParam = (String) job.getProperty("customParam");
        logger.info("Custom Param: {}", customParam);

        // Your content archival logic here

        // Return JobResult.OK if processing is successful
        return JobResult.OK;
    }
}