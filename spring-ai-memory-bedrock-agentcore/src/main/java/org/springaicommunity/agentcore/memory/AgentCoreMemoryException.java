package org.springaicommunity.agentcore.memory;

/**
 * Base exception for AgentCore Memory operations.
 */
public class AgentCoreMemoryException extends RuntimeException {

	public AgentCoreMemoryException(String message) {
		super(message);
	}

	public AgentCoreMemoryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Thrown when a memory retrieval operation fails.
	 */
	public static class RetrievalException extends AgentCoreMemoryException {

		public RetrievalException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * Thrown when a memory storage operation fails.
	 */
	public static class StorageException extends AgentCoreMemoryException {

		public StorageException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * Thrown when memory configuration is invalid.
	 */
	public static class ConfigurationException extends AgentCoreMemoryException {

		public ConfigurationException(String message) {
			super(message);
		}

	}

}
