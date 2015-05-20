package com.easemob.developer.github.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author stliu @ apache.org
 */
//@Component
//@Slf4j
public class JerseyConfig extends ResourceConfig {
//    @Autowired
    public JerseyConfig(ObjectMapper objectMapper) {
        packages(JerseyConfig.class.getPackage().getName());
        register(JacksonFeature.class);
        register(new JacksonJsonProvider(objectMapper));
        register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
//        if (log.isDebugEnabled()) {
            register(LoggingFilter.class);
//        }
    }
}
