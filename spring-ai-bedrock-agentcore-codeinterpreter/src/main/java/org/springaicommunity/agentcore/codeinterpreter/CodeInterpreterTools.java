/*
 * Copyright 2025-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springaicommunity.agentcore.codeinterpreter;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springaicommunity.agentcore.artifacts.ArtifactStore;
import org.springaicommunity.agentcore.artifacts.GeneratedFile;
import org.springaicommunity.agentcore.artifacts.SessionConstants;
import org.springframework.ai.model.tool.internal.ToolCallReactiveContextHolder;
import reactor.util.context.ContextView;

/**
 * Code Interpreter tool implementation for executing code in a secure sandbox.
 * <p>
 * This class contains the tool logic. Tool registration with configurable description is
 * handled by {@link AgentCoreCodeInterpreterAutoConfiguration}.
 *
 * @author Yuriy Bezsonov
 */
public class CodeInterpreterTools {

	private static final Logger logger = LoggerFactory.getLogger(CodeInterpreterTools.class);

	private static final Set<String> SUPPORTED_LANGUAGES = Set.of("python", "javascript", "typescript");

	public static final String DEFAULT_TOOL_DESCRIPTION = """
			Execute code in a secure sandbox environment.
			Supported languages: python, javascript, typescript.
			Use for calculations, data analysis, visualizations, file processing.
			Common libraries pre-installed (numpy, pandas, matplotlib for Python).
			For charts: use plt.savefig('name.png'). Use unique names for multiple charts.
			Generated files are automatically retrieved and displayed.
			""";

	private final AgentCoreCodeInterpreterClient client;

	private final ArtifactStore<GeneratedFile> artifactStore;

	public CodeInterpreterTools(AgentCoreCodeInterpreterClient client, ArtifactStore<GeneratedFile> artifactStore) {
		this.client = client;
		this.artifactStore = artifactStore;
		logger.debug("CodeInterpreterTools initialized");
	}

	/**
	 * Execute code in a secure sandbox environment.
	 * @param language programming language (python, javascript, typescript)
	 * @param code code to execute
	 * @return execution result text
	 */
	public String executeCode(String language, String code) {

		// Input validation
		if (language == null || language.isBlank()) {
			return "Error: language parameter is required (python, javascript, or typescript)";
		}
		String normalizedLanguage = language.toLowerCase().trim();
		if (!SUPPORTED_LANGUAGES.contains(normalizedLanguage)) {
			return "Error: unsupported language '" + language + "'. Supported: python, javascript, typescript";
		}
		if (code == null || code.isBlank()) {
			return "Error: code parameter is required";
		}

		// Get session ID from Reactor context (available via
		// ToolCallReactiveContextHolder)
		ContextView ctx = ToolCallReactiveContextHolder.getContext();
		String sessionId = ctx.getOrDefault(SessionConstants.SESSION_ID_KEY, SessionConstants.DEFAULT_SESSION_ID);

		logger.debug("executeCode called: language={}, sessionId={}, code:\n{}", normalizedLanguage, sessionId, code);

		CodeExecutionResult result = this.client.executeInEphemeralSession(normalizedLanguage, code);

		logger.debug("Result: {} chars text, {} files, isError={}", result.textOutput().length(), result.files().size(),
				result.isError());

		// Store files for ChatService to append later (keyed by session ID)
		if (result.hasFiles()) {
			this.artifactStore.storeAll(sessionId, result.files());
			logger.debug("Stored {} files for session {}", result.files().size(), sessionId);
		}

		return formatTextForLlm(result);
	}

	private String formatTextForLlm(CodeExecutionResult result) {
		StringBuilder sb = new StringBuilder();

		if (result.isError()) {
			sb.append("Error executing code:\n");
		}

		if (!result.textOutput().isEmpty()) {
			sb.append(result.textOutput());
		}

		// Describe files so LLM knows they exist
		for (GeneratedFile file : result.files()) {
			if (file.isImage()) {
				sb.append("\n[Chart generated: ").append(file.name()).append("]");
			}
			else {
				sb.append("\n[File generated: ").append(file.name()).append(" (").append(file.mimeType()).append(")]");
			}
		}

		if (sb.isEmpty()) {
			return "Code executed successfully (no output)";
		}

		return sb.toString();
	}

}
