package org.springaicommunity.agentcore.memory;

/**
 * Configuration for episodic memory strategy with separate strategies for episodes and
 * reflections.
 *
 * @author Yuriy Bezsonov
 */
public record AgentCoreLongMemoryEpisodicConfig(String strategyId, String reflectionsStrategyId, int episodesTopK,
		int reflectionsTopK, AgentCoreLongMemoryScope scope) implements AgentCoreLongMemoryStrategy {

	public AgentCoreLongMemoryEpisodicConfig {
		episodesTopK = episodesTopK > 0 ? episodesTopK : 3;
		reflectionsTopK = reflectionsTopK > 0 ? reflectionsTopK : 2;
		scope = scope != null ? scope : AgentCoreLongMemoryScope.ACTOR;
	}

	/**
	 * Check if reflections are enabled (separate strategy configured).
	 */
	public boolean hasReflections() {
		return reflectionsStrategyId != null && !reflectionsStrategyId.isEmpty();
	}

}
