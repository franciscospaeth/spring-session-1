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

final class InheritableThreadLocalSessionContextHolderStrategy implements SessionContextHolderStrategy {

	private static final ThreadLocal<SessionContext> contextHolder = new InheritableThreadLocal<SessionContext>();
	private static final ThreadLocal<Session> sessionHolder = new InheritableThreadLocal<Session>();

	public void clearContext() {
		contextHolder.remove();
		sessionHolder.remove();
	}

	public SessionContext getContext() {
		SessionContext ctx = contextHolder.get();

		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}

		return ctx;
	}

	public void setContext(SessionContext context) {
		if(context == null) {
			throw new IllegalArgumentException("context must not be null");
		}
		contextHolder.set(context);
	}

	public SessionContext createEmptyContext() {
		return new SessionContextBean();
	}

	public Session getSession() {
		return sessionHolder.get();
	}
	
	public void setSession(Session session) {
		sessionHolder.set(session);
	}

}
