package com.newrelic.labs.context;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;

public class NRHolder {
    private String segmentName = null;
    private Segment segment = null;
    private ExternalParameters params = null;
    private Token token = null;
    private boolean hasEnded = false;
    private boolean reportAsExternal = false;

    public NRHolder(Segment s, ExternalParameters p, boolean reportAsExternal) {
        segment = s;
        params = p;
        token = NewRelic.getAgent().getTransaction().getToken();
        hasEnded = false;
        this.reportAsExternal = reportAsExternal;
    }

    public NRHolder(String s, ExternalParameters p, boolean reportAsExternal) {
        segmentName = s;
        params = p;
        token = NewRelic.getAgent().getTransaction().getToken();
        hasEnded = false;
        this.reportAsExternal = reportAsExternal;
    }

    public void startSegment() {
        System.out.println("NEWRELIC-PLUGIN: NRHolder: startSegment(): " + segmentName);
        segment = NewRelic.getAgent().getTransaction().startSegment(segmentName);
    }

    public boolean hasEnded() {
        return hasEnded;
    }

    public void end() {
        System.out.println("NEWRELIC-PLUGIN: NRHolder: end()");
        if (token != null) {
            System.out.println("NEWRELIC-PLUGIN: NRHolder: end(): token.linkAndExpire()");
            token.linkAndExpire();
            token = null;
        }
        if (segment != null) {
            if (reportAsExternal) {
                System.out.println("NEWRELIC-PLUGIN: NRHolder: end(): segment : segment.reportAsExternal(params);");
                segment.reportAsExternal(params);
            }
            System.out.println("NEWRELIC-PLUGIN: NRHolder: end(): segment.end()");
            segment.end();
            params = null;
            segment = null;
        } else if (reportAsExternal) {
            System.out.println("NEWRELIC-PLUGIN: NRHolder: end(): NewRelic.getAgent().getTracedMethod().reportAsExternal(params)");
            NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
        }
    }
}