package com.easemob.developer.github.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author stliu @ apache.org
 */
@Component
public class PlainFileDataProcessor implements EventDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PlainFileDataProcessor.class);

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @Value("${data.file.path}")
    private String path;
    @Autowired
    private ObjectMapper objectMapper;
    private File file;

    @PostConstruct
    public void init() throws IOException {
        Path filePath = Paths.get(path);
        file = filePath.toFile();
        if (file == null) {
            throw new RuntimeException("Path " + path + " is invalid");
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new RuntimeException("Path " + path + " is a directory, which should be a file");
            }
        } else {
            Files.createParentDirs(file);
        }

    }

    @Override
    public void process(MultivaluedMap<String, String> headers, ContainerNode body) throws Exception {
        if (headers == null || body == null) {
            logger.warn("web hook is invalid, headers is  {} body is {}", headers, body);
            return;
        }

        Files.append("----------------------------------\r\n" + headers.toString()+ "\r\n" + objectMapper.writeValueAsString(body), file, UTF_8);;
    }
}
