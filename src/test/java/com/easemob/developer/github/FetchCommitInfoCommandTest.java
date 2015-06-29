package com.easemob.developer.github;

import com.easemob.developer.github.request.FetchCommitInfoCommand;
import com.easemob.developer.github.rest.CommitRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author stliu at apache.org
 * @since 6/1/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@WebIntegrationTest
@IntegrationTest
@SpringApplicationConfiguration(
        classes = GithubConfiguration.class,
        initializers = {ConfigFileApplicationContextInitializer.class,
                ConfigurationWarningsApplicationContextInitializer.class}
)
//@ContextConfiguration(loader = SpringApplicationContextLoader.class)

//@WebIntegrationTest("server.port:0")

public class FetchCommitInfoCommandTest {
    @Autowired
    FetchCommitInfoCommand commitInfoCommand;
//
//    @Autowired
//    EmbeddedWebApplicationContext server;
//
//    @Value("${local.server.port}")
//    int port;

//    @Test
//    public void testFetchCommitInfo() {
//        CommitRequest request = new CommitRequest();
//        request.setOwner("easemob");
//        request.setRepo("usergrid");
//        request.setSha("0d54fba");
//        commitInfoCommand.fetch(request);
//    }
}
