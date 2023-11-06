/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.techrevel.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = {Servlet.class},
        property = {
                "sling.servlet.paths=/bin/generateCatalog",
                "sling.servlet.methods=GET",
        })
public class ProductCatalogServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private final String TOPIC = "product/catalog/generate";

    @Reference
    private JobManager jobManager;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
        resp.setContentType("text/plain");
        generateProductCatalog("/a/b", "ABC_criterion");
        resp.getWriter().write("Job Scheduled for: " + TOPIC);
    }

    public void generateProductCatalog(String catalogPath, String criteria) {

        Map<String, Object> jobProperties = new HashMap<>();
        jobProperties.put("catalogPath", catalogPath);
        jobProperties.put("criteria", criteria);

        // Trigger the job to render and publish the product catalog
        jobManager.addJob(TOPIC, jobProperties);
    }
}
