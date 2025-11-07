package AI;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

import java.io.FileWriter;
import java.time.Duration;
import java.util.List;

public class AIReporter {

	public static void generateReport(List<String> logs) {
		String joinedLogs = String.join("\n", logs);
		String apiKey = System.getenv("OPENAI_API_KEY"); // set this in Jenkins as a secret env variable
		OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));

		CompletionRequest request = CompletionRequest.builder().model("gpt-4-turbo")
				.prompt("Summarize this Selenium test execution log in a short executive report:\n\n" + joinedLogs)
				.maxTokens(300).build();

		String summary = service.createCompletion(request).getChoices().get(0).getText();

		try (FileWriter writer = new FileWriter("reports/ai_summary.txt")) {
			writer.write("===== AI-Generated Test Summary =====\n");
			writer.write(summary);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
