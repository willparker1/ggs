package com.porkerspicks.ggs.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.porkerspicks.ggs.service.events.BetEvent;

@Component
public class BetEventListener {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@EventListener
	public void onApplicationEvent(BetEvent betEvent) {
		log.info("Received Category Event : " + betEvent.getEventType());
		log.info("Received Category From Category Event :" + betEvent.getBet().toString());
	}
}

