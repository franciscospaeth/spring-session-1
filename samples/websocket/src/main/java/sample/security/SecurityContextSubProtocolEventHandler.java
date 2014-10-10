/*
 * Copyright 2002-2013 the original author or authors.
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
package sample.security;

import java.security.Principal;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.GenericApplicationListenerAdapter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

public class SecurityContextSubProtocolEventHandler<E extends AbstractSubProtocolEvent> extends GenericApplicationListenerAdapter {

    public SecurityContextSubProtocolEventHandler(ApplicationListener<E> listener) {
        super(listener);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent e) {
        E event = (E) e;
        MessageHeaders headers = event.getMessage().getHeaders();
        Principal user = SimpMessageHeaderAccessor.getUser(headers);
        if(!(user instanceof Authentication)) {
            super.onApplicationEvent(event);
            return;
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication((Authentication) user);
        try {
            SecurityContextHolder.setContext(context);
            super.onApplicationEvent(event);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}