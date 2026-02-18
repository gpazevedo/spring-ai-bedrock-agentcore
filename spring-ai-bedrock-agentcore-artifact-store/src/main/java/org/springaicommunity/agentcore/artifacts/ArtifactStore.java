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

package org.springaicommunity.agentcore.artifacts;

import java.util.List;

/**
 * Session-scoped storage for tool-generated artifacts.
 * <p>
 * Stores artifacts (files, screenshots, etc.) keyed by session ID. Supports concurrent
 * access in multi-user environments. Artifacts are retrieved once and cleared (one-time
 * consumption pattern).
 * <p>
 * <b>Thread Safety:</b> Implementations must be thread-safe. Multiple threads may
 * concurrently store and retrieve artifacts for different sessions. Operations on the
 * same session ID must be atomic.
 *
 * @param <T> the type of artifact to store
 * @author Yuriy Bezsonov
 */
public interface ArtifactStore<T> {

	/**
	 * Store a single artifact for a session.
	 * @param sessionId the session ID (use {@link SessionConstants#DEFAULT_SESSION_ID}
	 * for single-user)
	 * @param artifact the artifact to store
	 */
	void store(String sessionId, T artifact);

	/**
	 * Store multiple artifacts for a session.
	 * @param sessionId the session ID
	 * @param artifacts list of artifacts to store
	 */
	void storeAll(String sessionId, List<T> artifacts);

	/**
	 * Retrieve and clear stored artifacts for a session (destructive read).
	 * @param sessionId the session ID
	 * @return list of artifacts, or null if none stored
	 */
	List<T> retrieve(String sessionId);

	/**
	 * Peek at stored artifacts without removing them (non-destructive read).
	 * <p>
	 * Useful for debugging, logging, or inspection without consuming artifacts.
	 * @param sessionId the session ID
	 * @return unmodifiable list of artifacts, or null if none stored
	 */
	List<T> peek(String sessionId);

	/**
	 * Check if artifacts are stored for a session.
	 * @param sessionId the session ID
	 * @return true if artifacts are available
	 */
	boolean hasArtifacts(String sessionId);

	/**
	 * Get the count of artifacts stored for a session.
	 * @param sessionId the session ID
	 * @return number of artifacts, or 0 if none stored
	 */
	int count(String sessionId);

	/**
	 * Clear stored artifacts for a session without returning them.
	 * <p>
	 * Useful for cleanup scenarios where artifacts don't need to be processed.
	 * @param sessionId the session ID
	 */
	void clear(String sessionId);

}
