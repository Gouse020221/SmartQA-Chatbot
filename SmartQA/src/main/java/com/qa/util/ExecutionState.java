package com.qa.util;

import java.util.HashSet;
import java.util.Set;

public class ExecutionState {

	public static final Set<String> executedSteps = new HashSet<>();

	public static boolean isStepExecuted(String step) {
		//System.out.println("Alre");
		return executedSteps.contains(step);
	}

	public static void markStepAsExecuted(String step) {
		executedSteps.add(step);
	}
}
