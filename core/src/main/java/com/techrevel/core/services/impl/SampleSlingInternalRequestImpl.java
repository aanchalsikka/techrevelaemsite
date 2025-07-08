package com.techrevel.core.services.impl;

import com.techrevel.core.services.SampleSlingInternalRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.servlethelpers.internalrequests.SlingInternalRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Service implementation to demonstrate how to internally call AEM Servlets
 * (both POST and GET) using Sling's internal request simulation.
 *
 * <p>This class uses a service user to obtain a secure ResourceResolver,
 * and then leverages {@link SlingInternalRequest} to programmatically simulate
 * HTTP calls to other servlets within the same AEM instance, without going through
 * an external HTTP client.</p>
 */
@Component(
        service = SampleSlingInternalRequest.class,
        immediate = true
)
public class SampleSlingInternalRequestImpl implements SampleSlingInternalRequest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private SlingRequestProcessor slingRequestProcessor;

    /** Name of the service user (must be mapped in user mapping config) */
    private static final String SUBSERVICE_NAME = "service-user";

    /**
     * Executes internal GET and POST servlet calls using SlingInternalRequest.
     *
     * <p>Use cases include automation, system-to-system communication,
     * and calling internal endpoints without exposing them externally.</p>
     */
    @Override
    public void callServlets() {
        // Create a map to pass the subservice user to obtain a system-level ResourceResolver
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE_NAME);

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {

            // --- POST Servlet Call ---
            // Simulates a POST request to /bin/simplePostServlet with parameters
            String postResponse = new SlingInternalRequest(resolver, slingRequestProcessor, "/bin/simplePostServlet")
                    .withParameter("name", "Aanchal")
                    .withParameter("email", "abc@gmail.com")
                    .withRequestMethod("POST")
                    .execute()
                    .checkStatus(200) // Validates that response status is 200
                    .checkResponseContentType("text/plain")
                    .getResponseAsString(); // Retrieves the servlet response as a string

            logger.info("Response from POST: {}", postResponse);


            // --- GET Servlet Call ---
            // Simulates a GET request to a specific page content
            String getResponse = new SlingInternalRequest(resolver, slingRequestProcessor,
                    "/content/we-retail/language-masters/en/jcr:content")
                    .withExtension("txt") // Appends .txt extension to the path
                    .withRequestMethod("GET")
                    .execute()
                    .checkStatus(200)
                    .checkResponseContentType("text/plain")
                    .getResponseAsString();

            logger.info("Response from GET: {}", getResponse);

        } catch (LoginException e) {
            logger.error("Service user login failed. Check user-mapping in Apache Sling Service User Mapper.", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while calling internal servlets.", e);
        }
    }
}
