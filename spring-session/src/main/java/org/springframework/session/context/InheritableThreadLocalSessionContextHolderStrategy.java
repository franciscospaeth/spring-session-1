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

final class InheritableThreadLocalSessionContextHolderStrategy
		implements SessionContextHolderStrategy {

	private static final ThreadLocal<SessionContextTuple> contextHolder = new InheritableThreadLocal<SessionContextTuple>();

	public void clear() {
		contextHolder.remove();
	}

	public SessionContext getContext() {
		return getTuple().getContext();
	}

	public void setContext(SessionContext context) {
		getTuple().setContext(context);
	}

	public Session getSession() {
		return getTuple().getSession();
	}

	public void setSession(Session session) {
		getTuple().setSession(session);
	}

	private SessionContextTuple getTuple() {
		SessionContextTuple tuple = contextHolder.get();
		if (tuple == null) {
			tuple = new SessionContextTuple();
			contextHolder.set(tuple);
		}
		return tuple;
	}

}
