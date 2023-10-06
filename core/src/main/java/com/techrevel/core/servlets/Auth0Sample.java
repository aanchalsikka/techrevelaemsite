package com.techrevel.core.servlets;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.drew.lang.annotations.NotNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;

@Component(service = Servlet.class, property = {
        "sling.servlet.resourceTypes=" + "sling/servlet/default",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.selectors=image",
        "sling.servlet.extensions=" + "jpg"

})
public class Auth0Sample extends SlingSafeMethodsServlet {

    protected void doGet(final @NotNull SlingHttpServletRequest request, final @NotNull SlingHttpServletResponse response) throws AlgorithmMismatchException {
        throw new AlgorithmMismatchException("Just a sample to enforce dependency on JWT bundle");
    }
}