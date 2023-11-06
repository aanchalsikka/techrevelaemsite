package com.techrevel.core.schedulers;

import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.ScheduledJobInfo;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.JobBuilder.ScheduleBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true)
// Job Scheduler
public class ContentArchivalJobSchedular {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String TOPIC = "content/archival/scheduled";

    @Reference
    private JobManager jobManager;

    @Activate
    protected void activate() {
        //Call this if you want to change schedule of the job. It will unregister and register again
        //stopScheduledJob();
        startScheduledJob();
    }

    public void stopScheduledJob() {
        Collection<ScheduledJobInfo> myJobs = jobManager.getScheduledJobs(TOPIC, 10, null);
        myJobs.forEach(sji -> sji.unschedule());
    }

    public void startScheduledJob() {
        // Check if the scheduled job already exists.
        Collection<ScheduledJobInfo> myJobs = jobManager.getScheduledJobs(TOPIC, 1, null);
        if (myJobs.isEmpty()) {
            //Setting some properties to pass to the JOb
            Map<String, Object> jobProperties = new HashMap<>();
            jobProperties.put("customParam", "Some_custom_value");

            // Daily invocation not yet scheduled
            JobBuilder jobBuilder = jobManager.createJob(TOPIC);
            jobBuilder.properties(jobProperties);

            ScheduleBuilder scheduleBuilder = jobBuilder.schedule();
//            scheduleBuilder.cron("0 * * * * ?"); // Every minute
            scheduleBuilder.daily(0, 0)// Execute daily at midnight

            if (scheduleBuilder.add() == null) {
                // Something went wrong here, use scheduleBuilder.add(List<String>) instead to get further information about the error
            } else {
                logger.info("Job scheduled for: {}", TOPIC);
            }
        }
    }
}