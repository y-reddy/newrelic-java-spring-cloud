package com.newrelic.labs.consumer;

import com.newrelic.labs.context.NRHolder;
import org.reactivestreams.Subscription;

import java.util.function.Consumer;

public class NRSubscribeConsumer implements Consumer<Subscription> {
	
	private NRHolder holder = null;
	
	public NRSubscribeConsumer(NRHolder h) {
		holder = h;
	}

	@Override
	public void accept(Subscription t) {
		if(holder != null) {
			holder.startSegment();
		}
	}

}