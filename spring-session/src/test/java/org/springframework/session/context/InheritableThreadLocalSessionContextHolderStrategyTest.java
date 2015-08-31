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

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.session.Session;

public class InheritableThreadLocalSessionContextHolderStrategyTest {

	private SessionContextHolderStrategy strategy = new InheritableThreadLocalSessionContextHolderStrategy();

	@Test
	public void testSetterGetterAndClear() {
		SessionContext context = Mockito.mock(SessionContext.class);
		Session session = Mockito.mock(Session.class);

		strategy.setContext(context);
		strategy.setSession(session);
		SessionContext retrievedContext = strategy.getContext();
		Session retrievedSession = strategy.getSession();

		strategy.clear();
		SessionContext retrievedContextAfterClear = strategy.getContext();
		Session retrievedSessionAfterClear = strategy.getSession();

		Assert.assertEquals(retrievedContext, context);
		Assert.assertEquals(retrievedSession, session);
		Assert.assertNull(retrievedContextAfterClear);
		Assert.assertNull(retrievedSessionAfterClear);
	}

	@Test
	public void testInheritedThreadsContextHolder() throws Exception {
		final AtomicBoolean failed = new AtomicBoolean(true);

		final SessionContext context = Mockito.mock(SessionContext.class);
		final Session session = Mockito.mock(Session.class);
		
		strategy.setContext(context);
		strategy.setSession(session);

		Thread thread = new Thread(new Runnable() {
			public void run() {
				if (strategy.getSession() == session
						|| strategy.getContext() == context) {
					failed.set(false);
				}
			}
		});
		thread.start();
		thread.join();
		Assert.assertFalse("no inheritable thread strategy seems to be applied",
				failed.get());
	}

	@Test
	public void testContextSetup() {
		SessionContext context = Mockito.mock(SessionContext.class);
		strategy.setContext(context);
		strategy.getSession();

		Mockito.verify(context).setup();
	}

}
