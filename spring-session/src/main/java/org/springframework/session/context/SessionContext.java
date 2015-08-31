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
 * A {@link Session} context, used to specify instructions necessary to setup
 * the session from its context.
 * 
 * @author Francisco Spaeth
 * @since 1.1
 *
 */
public interface SessionContext {

	/**
	 * Configures the session from its context. In a web context, it will typically trigger the creation of an {@link javax.servlet.http.HttpSession}.
	 */
	void setup();

}
