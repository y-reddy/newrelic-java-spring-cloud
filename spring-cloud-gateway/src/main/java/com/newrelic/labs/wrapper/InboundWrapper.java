package com.newrelic.labs.wrapper;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;
import org.springframework.http.HttpHeaders;

public class InboundWrapper implements InboundHeaders {

        private final HttpHeaders httpHeaders;

        // OutboundHeaders is implemented by delegating to the library's response object
        public InboundWrapper(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
        }

        // New Relic CAT specifies different header names for HTTP and MESSAGE
        @Override
        public HeaderType getHeaderType() {
            return HeaderType.HTTP;
        }

        // this allows the agent to read the correct headers from the HTTP response
       @Override
       public String getHeader(String name) {
           return httpHeaders.getFirst(name);
       }
    }