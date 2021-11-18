package com.newrelic.labs.consumer;

import com.newrelic.labs.context.NRHolder;
import reactor.core.publisher.SignalType;

import java.util.function.Consumer;

public class NRSignalTypeConsumer implements Consumer<SignalType> {

	private NRHolder holder = null;
	
	public NRSignalTypeConsumer(NRHolder h) {
		holder = h;
	}

	@Override
	public void accept(SignalType t) {
		System.out.println("NEWRELIC-PLUGIN: NRSignalTypeConsumer: accept");
		if(holder != null && !holder.hasEnded()) {
			System.out.println("NEWRELIC-PLUGIN: NRSignalTypeConsumer: holder.end()");
			holder.end();
		}
	}

}