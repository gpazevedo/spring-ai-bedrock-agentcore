package org.springaicommunity.agentcore.memory;

/**
 * Configuration for summary memory strategy.
 *
 * @author Yuriy Bezsonov
 */
public record AgentCoreLongMemorySummaryConfig(String strategyId, int topK,
		AgentCoreLongMemoryScope scope) implements AgentCoreLongMemoryStrategy {

	public AgentCoreLongMemorySummaryConfig {
		topK = topK > 0 ? topK : 3;
		scope = scope != null ? scope : AgentCoreLongMemoryScope.SESSION;
	}

}
