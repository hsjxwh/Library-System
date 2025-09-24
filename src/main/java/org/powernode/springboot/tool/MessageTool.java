package org.powernode.springboot.tool;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class MessageTool {
    public static MultiValueMap<String,String> getParams(URI uri){
        return UriComponentsBuilder.fromUri(uri).build().getQueryParams();
    }
}

