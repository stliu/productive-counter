package com.easemob.developer.github.data;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplateHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * This is used to resolve Github API template.
 *
 * Github returns something like https://api.github.com/repos/stliu/test/commits{/sha}
 *
 * @author stliu <stliu@apache.org>
 * @date 5/20/15
 */
public class GithubUriTemplateHandler implements UriTemplateHandler {
    private boolean parsePath;


    /**
     * Whether to parse the path of a URI template string into path segments.
     * <p>If set to {@code true} the path of parsed URI templates is decomposed
     * into path segments so that URI variables expanded into the path are
     * treated according to path segment encoding rules. In effect that means the
     * "/" character is percent encoded.
     * <p>By default this is set to {@code false} in which case the path is kept
     * as a full path and expanded URI variables will preserve "/" characters.
     * @param parsePath whether to parse the path into path segments
     */
    public void setParsePath(boolean parsePath) {
        this.parsePath = parsePath;
    }

    /**
     * Whether the handler is configured to parse the path into path segments.
     */
    public boolean shouldParsePath() {
        return this.parsePath;
    }


    @Override
    public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
        UriComponentsBuilder builder = initBuilder(uriTemplate);
        return builder.build().expand(uriVariables).encode().toUri();
    }

    @Override
    public URI expand(String uriTemplate, Object... uriVariableValues) {
        UriComponentsBuilder builder = initBuilder(uriTemplate);
        return builder.build().expand(uriVariableValues).encode().toUri();
    }

    protected UriComponentsBuilder initBuilder(String uriTemplate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
        if (shouldParsePath()) {
            List<String> pathSegments = builder.build().getPathSegments();
            builder.replacePath(null);
            for (String pathSegment : pathSegments) {
                builder.pathSegment(pathSegment);
            }
        }
        return builder;
    }


}
