package com.StepImplementation;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class CustomLogCollector extends AppenderBase<ILoggingEvent> {

	private static final ThreadLocal<List<String>> threadLogs = ThreadLocal.withInitial(ArrayList::new);

	@Override
	protected void append(ILoggingEvent eventObject) {
		threadLogs.get().add(eventObject.getFormattedMessage());
	}

	public static List<String> getLogs() {
		return new ArrayList<>(threadLogs.get()); // Return a copy to prevent modification
	}

	public static void clearLogs() {
		threadLogs.get().clear();
	}
}