package org.springaicommunity.agentcore.memory;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for long-term memory strategies.
 *
 * @author Yuriy Bezsonov
 */
@ConfigurationProperties(prefix = "agentcore.memory.long-term")
public record AgentCoreLongMemoryConfiguration(boolean enabled, AgentCoreLongMemorySemanticConfig semantic,
		AgentCoreLongMemoryUserPreferenceConfig userPreference, AgentCoreLongMemorySummaryConfig summary,
		AgentCoreLongMemoryEpisodicConfig episodic) {
}
