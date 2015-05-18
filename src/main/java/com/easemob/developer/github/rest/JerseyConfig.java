package com.easemob.developer.github.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * @author stliu @ apache.org
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages(JerseyConfig.class.getPackage().getName());
    }

}