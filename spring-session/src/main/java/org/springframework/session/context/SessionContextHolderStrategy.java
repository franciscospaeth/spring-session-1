/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.session.context;

import org.springframework.session.Session;

/**
 * A strategy for storing {@link Session} and {@link SessionContext} information.
 *
 * <p>
 * The strategy instance is mainly used by {@link SessionContextHolder}.
 * </p>
 *
 * @author Francisco Spaeth
 * @since 1.1
 *
 */
public interface SessionContextHolderStrategy {

	/**
	 * Clears the current session and context.
	 */
	void clear();

	/**
	 * Obtains the current session context.
	 *
	 * @return a context (never <code>null</code> - create a default implementation if
	 * necessary)
	 */
	SessionContext getContext();

	/**
	 * Sets the current session context.
	 *
	 * @param context to the new argument
	 */
	void setContext(SessionContext context);
	
	/**
	 * Sets the current session.
	 * 
	 * @param session
	 */
	void setSession(Session session);
	
	/**
	 * Obtains the current session.
	 * 
	 * @return
	 */
	Session getSession();

}
