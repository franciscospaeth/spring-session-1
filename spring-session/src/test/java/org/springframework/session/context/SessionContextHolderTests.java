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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.session.Session;

public class SessionContextHolderTests {

	@BeforeClass
	public static void beforeClass() {
		SessionContextHolder.setStrategyName(MySessionContextHolderStrategy.class.getName());
	}

	@AfterClass
	public static void afterClass() {
		SessionContextHolder.setStrategyName(SessionContextHolder.MODE_THREADLOCAL);
	}

	@Test
	public void testContextHolderSetter() {
		SessionContext mockedSessionContext = Mockito.mock(SessionContext.class);
		SessionContextHolder.setContext(mockedSessionContext);

		Mockito.verify(MySessionContextHolderStrategy.delegate).setContext(mockedSessionContext);
	}

	@Test
	public void testContextHolderClearContextDelegatedToStrategy() {
		SessionContextHolder.clearContext();

		Mockito.verify(MySessionContextHolderStrategy.delegate).clearContext();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRejectNull() {
		SessionContextHolder.setContext(null);
	}

	public static class MySessionContextHolderStrategy implements SessionContextHolderStrategy {

		static SessionContextHolderStrategy delegate;

		public MySessionContextHolderStrategy() {
			delegate = Mockito.mock(SessionContextHolderStrategy.class);
		}

		public void clearContext() {
			delegate.clearContext();
		}

		@Deprecated
		public SessionContext getContext() {
			return delegate.getContext();
		}

		@Deprecated
		public void setContext(SessionContext context) {
			delegate.setContext(context);
		}

		@Deprecated
		public SessionContext createEmptyContext() {
			return delegate.createEmptyContext();
		}

		public void setSession(Session session) {
			delegate.setSession(session);
		}
		
		public Session getSession() {
			return delegate.getSession();
		}
	
	}

}
