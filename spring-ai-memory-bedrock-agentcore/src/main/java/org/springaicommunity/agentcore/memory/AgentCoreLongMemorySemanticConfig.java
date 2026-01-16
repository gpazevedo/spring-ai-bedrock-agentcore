package org.springaicommunity.agentcore.memory;

/**
 * Configuration for semantic memory strategy.
 *
 * @author Yuriy Bezsonov
 */
public record AgentCoreLongMemorySemanticConfig(String strategyId, int topK,
		AgentCoreLongMemoryScope scope) implements AgentCoreLongMemoryStrategy {

	public AgentCoreLongMemorySemanticConfig {
		topK = topK > 0 ? topK : 3;
		scope = scope != null ? scope : AgentCoreLongMemoryScope.ACTOR;
	}

}
