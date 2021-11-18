package com.newrelic.labs;

import com.newrelic.api.agent.*;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.labs.consumer.NRErrorConsumer;
import com.newrelic.labs.consumer.NRSignalTypeConsumer;
import com.newrelic.labs.consumer.NRSubscribeConsumer;
import com.newrelic.labs.context.NRHolder;
import com.newrelic.labs.wrapper.InboundWrapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.net.URI;
import java.util.function.Consumer;
import java.util.logging.Level;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Weave(type = MatchType.ExactClass, originalName = "org.springframework.cloud.gateway.filter.NettyRoutingFilter")
public class NettyRoutingFilter_Instrumentation {

    final String library = "HttpClient";
    final String procedure = "execute";

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Mono<Void> result = Weaver.callOriginal();

        try {
            URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);

            // construct external parameters
            ExternalParameters params = HttpParameters
                    .library(library)
                    .uri(requestUrl)
                    .procedure(procedure)
                    .inboundHeaders(new InboundWrapper(exchange.getRequest().getHeaders()))
                    .build();
            NRHolder holder = new NRHolder("externalServiceCall", params, true);
            NRSubscribeConsumer subscriberConsumer = new NRSubscribeConsumer(holder);

            NRErrorConsumer errorConsumer = new NRErrorConsumer(holder);
            Consumer<SignalType> onFinally = new NRSignalTypeConsumer(holder);
            result.doOnSubscribe(subscriberConsumer).doOnError(errorConsumer).doFinally(onFinally);
        } catch (Exception e) {
            NewRelic.getAgent().getLogger().log(Level.SEVERE, e, "Error in NettyRoutingFilter instrumentation");
        }
        return result;
    }
}
