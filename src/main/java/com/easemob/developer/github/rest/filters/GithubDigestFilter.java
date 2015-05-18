package com.easemob.developer.github.rest.filters;

import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.apache.cassandra.utils.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author stliu @ apache.org
 */
//@Component
//@Provider
public class GithubDigestFilter implements ContainerRequestFilter {
    public static final String GIT_SIGNATURE_HEADER = "X-Hub-Signature";

    @Value("${github.webhook.secret:}")
    private String webhookSecret;
    HMacSha1Encoder encoder;
    @PostConstruct
    public void init(){
        if(Strings.isNullOrEmpty(webhookSecret)){
            try {
                encoder = new HMacSha1Encoder(webhookSecret);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> list = Files.readLines(new File("/Users/stliu/a.txt"), Charset.forName("UTF-8"));

        String value = "";
        for (String s : list) {
            value = value + s;
        }
        String key = "1234567890";
        HMacSha1Encoder encoder = new HMacSha1Encoder(key);
        System.out.println(encoder.encode(value));
        System.out.println("ce699cb0bb5cedebc09972f91547060611048271");
        for (int i = 0; i < 10; i++)
            System.out.println(encoder.encode(value));


    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (encoder==null || !HttpMethod.POST.equalsIgnoreCase(requestContext.getMethod())) {
            //we only care about POST request, which come from the github webhook
            return;
        }
        final String value = requestContext.getHeaderString(GIT_SIGNATURE_HEADER);
        if (Strings.isNullOrEmpty(value) || !value.startsWith("sha1=")) {
            return;
        }
        final String sha1 = value.substring(5);
//        requestContext.
    }
}
