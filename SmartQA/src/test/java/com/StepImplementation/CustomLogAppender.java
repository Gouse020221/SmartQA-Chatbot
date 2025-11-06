package com.StepImplementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class CustomLogAppender extends AppenderBase<ILoggingEvent> {

	private static final Logger logger = LoggerFactory.getLogger(CustomLogAppender.class);

	@Override
	protected void append(ILoggingEvent eventObject) {
		// Custom logic for appending logs
		logger.info("Custom Appender: " + eventObject.getFormattedMessage());
	}
}
