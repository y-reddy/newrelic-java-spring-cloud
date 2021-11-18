package com.newrelic.labs.consumer;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.labs.context.NRHolder;

import java.util.function.Consumer;

public class NRErrorConsumer implements Consumer<Throwable> {

	private NRHolder holder = null;

	public NRErrorConsumer(NRHolder h) {
		holder = h;
	}
	
	@Override
	public void accept(Throwable t) {
		NewRelic.noticeError(t);
		if(holder != null && !holder.hasEnded()) {
			holder.end();
		}
	}


}
