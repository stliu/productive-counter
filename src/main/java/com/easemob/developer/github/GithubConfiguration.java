package com.easemob.developer.github;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.config.TaskExecutorFactoryBean;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/19/15
 */
@Configuration
@Slf4j
public class GithubConfiguration {

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${tasks.pool.size:50}")
    private int poolSize = 50;
    @Value("${tasks.pool.capacity:0x7fffffff}")
    private int poolCapacity = 0x7fffffff;


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
        client.register(JacksonFeature.class);
        client.register(new JacksonJsonProvider(objectMapper));
        client.register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        if (log.isDebugEnabled()) {
        client.register(LoggingFilter.class);
        }
        return client;
    }

//    @Bean
//    public AsyncRestTemplate asyncRestTemplate() {
//        AsyncRestTemplate template = new AsyncRestTemplate(netty4ClientHttpRequestFactory());
//        return template;
//    }
//
//    public Netty4ClientHttpRequestFactory netty4ClientHttpRequestFactory() {
//        Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory();
//        return requestFactory;
//    }

}
