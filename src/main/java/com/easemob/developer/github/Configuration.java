package com.easemob.developer.github;

import com.easemob.developer.github.rest.GithubWebhookResource;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.TaskExecutorFactoryBean;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.util.ErrorHandler;

import javax.ws.rs.core.Configurable;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/19/15
 */
@org.springframework.context.annotation.Configuration
@Slf4j
public class Configuration {
    public static final String GITHUB_V3_MEDIA_TYPE = "application/vnd.github.v3+json";

    @Value("${github.access.token:}")
    private String githubAccessToken;
    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${tasks.pool.size:50}")
    private int poolSize;
    @Value("${tasks.pool.capacity:0x7fffffff}")
    private int poolCapacity;


    @Bean
    public ExecutorServiceAdapter executorServiceAdapter(TaskExecutor executor) {
        return new ExecutorServiceAdapter(executor);

    }

    @Bean
    public TaskExecutorFactoryBean threadPoolTaskScheduler() {
        TaskExecutorFactoryBean factoryBean = new TaskExecutorFactoryBean();
        factoryBean.setPoolSize(String.valueOf(poolSize));
        factoryBean.setQueueCapacity(poolCapacity);
        factoryBean.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                super.rejectedExecution(r, e);
                log.warn("TaskExecutor is over capacity, starting to reject oldest tasks");
            }
        });
        return factoryBean;

    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return mapper;

    }

    @Bean
    public RxClient<RxObservableInvoker> rxclient(ObjectMapper objectMapper) {
        RxClient<RxObservableInvoker> client = Rx.newClient(RxObservableInvoker.class);
        configJersey(client, objectMapper);
        return client;
    }

    @Bean
    public ResourceConfig resourceConfig(ObjectMapper objectMapper, GithubWebhookResource resource) {
        ResourceConfig resourceConfig = new ResourceConfig();
        configJersey(resourceConfig, objectMapper);
        resourceConfig.register(resource);
        resourceConfig.packages(Configuration.class.getPackage().getName());
        return resourceConfig;
    }

    private <T extends Configurable> void configJersey(T configurable, ObjectMapper objectMapper) {
        configurable.register(JacksonFeature.class);
        configurable.register(new JacksonJsonProvider(objectMapper));
        configurable.register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
//        if (log.isDebugEnabled()) {
            configurable.register(LoggingFilter.class);
//        }
    }
}
