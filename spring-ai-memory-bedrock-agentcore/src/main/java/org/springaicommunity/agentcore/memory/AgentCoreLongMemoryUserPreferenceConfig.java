package org.springaicommunity.agentcore.memory;

/**
 * Configuration for user preference memory strategy. No topK because preferences are
 * listed (not searched) to ensure all preferences apply regardless of query relevance.
 *
 * @author Yuriy Bezsonov
 */
public record AgentCoreLongMemoryUserPreferenceConfig(String strategyId,
		AgentCoreLongMemoryScope scope) implements AgentCoreLongMemoryStrategy {

	public AgentCoreLongMemoryUserPreferenceConfig {
		scope = scope != null ? scope : AgentCoreLongMemoryScope.ACTOR;
	}

}
