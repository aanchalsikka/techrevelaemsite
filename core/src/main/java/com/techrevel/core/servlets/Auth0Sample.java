package com.techrevel.core.servlets;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
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
    private static final String AUTH0_DOMAIN = "https://abc-78zahjrcz4ydsczg.us.auth0.com/";

    protected void doGet(final @NotNull SlingHttpServletRequest request, final @NotNull SlingHttpServletResponse response) {
        JwkProvider provider = new UrlJwkProvider(AUTH0_DOMAIN);
    }
}