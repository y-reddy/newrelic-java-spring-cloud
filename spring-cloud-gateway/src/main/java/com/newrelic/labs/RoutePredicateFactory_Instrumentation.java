package com.newrelic.labs;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

@Weave(type = MatchType.Interface, originalName = "org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory")
public abstract class RoutePredicateFactory_Instrumentation<C> {

    @Trace
    public Predicate<ServerWebExchange> apply(C config) {
        return Weaver.callOriginal();
    }
}
