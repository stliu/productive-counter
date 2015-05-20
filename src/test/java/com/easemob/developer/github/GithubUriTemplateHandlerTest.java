package com.easemob.developer.github;

import com.easemob.developer.github.data.GithubUriTemplateHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * GithubUriTemplateHandler Tester.
 */
public class GithubUriTemplateHandlerTest {


    /**
     * Method: setParsePath(boolean parsePath)
     */
    @Test
    public void testSetParsePath() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: shouldParsePath()
     */
    @Test
    public void testShouldParsePath() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: expand(String uriTemplate, Map<String, ?> uriVariables)
     */
    @Test
    public void testExpandForUriTemplateUriVariables() throws Exception {
        GithubUriTemplateHandler templateHandler = new GithubUriTemplateHandler();
        templateHandler.setParsePath(true);
        Map<String, String> vars = new HashMap<>();
        vars.put("sha", "1234");
        URI uri = templateHandler.expand("https://api.github.com/repos/stliu/test/commits{/sha}", vars);
        Assert.assertEquals(URI.create("https://api.github.com/repos/stliu/test/commits/1234"), uri);
    }

    /**
     * Method: expand(String uriTemplate, Object... uriVariableValues)
     */
    @Test
    public void testExpandForUriTemplateUriVariableValues() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: initBuilder(String uriTemplate)
     */
    @Test
    public void testInitBuilder() throws Exception {
//TODO: Test goes here... 
    }


} 
