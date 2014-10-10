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
package sample.config;

import java.util.Stack;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * <p>
 * Creates a {@link ExecutorChannelInterceptor} that will obtain the {@link Authentication} from the specified
 * {@link Message#getHeaders()}.
 * </p>
 *
 * @since 4.0
 * @author Rob Winch
 */
public final class MessageUserToSecurityContextChannelInterceptor extends ChannelInterceptorAdapter implements ExecutorChannelInterceptor {
    private final String authenticationHeaderName;

    private static final ThreadLocal<Stack<SecurityContext>> CONTEXT_STACK = new ThreadLocal<Stack<SecurityContext>>();

    private static final SecurityContext EMPTY_CONTEXT = SecurityContextHolder.createEmptyContext();

    /**
     * Creates a new instance using the header of the name {@link SimpMessageHeaderAccessor#USER_HEADER}.
     */
    public MessageUserToSecurityContextChannelInterceptor() {
        this(SimpMessageHeaderAccessor.USER_HEADER);
    }

    /**
     * Creates a new instance that uses the specified header to obtain the {@link Authentication}.
     *
     * @param authenticationHeaderName the header name to obtain the {@link Authentication}. Cannot be null.
     */
    public MessageUserToSecurityContextChannelInterceptor(String authenticationHeaderName) {
        Assert.notNull(authenticationHeaderName, "authenticationHeaderName cannot be null");
        this.authenticationHeaderName = authenticationHeaderName;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return setup(message);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        cleanup();
    }

    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        return setup(message);
    }

    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        cleanup();
    }

    private Message<?> setup(Message<?> message) {
        Object user = message.getHeaders().get(authenticationHeaderName);
        if(!(user instanceof Authentication)) {
            return message;
        }
        Authentication authentication = (Authentication) user;
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authentication);

        SecurityContext originalContext = SecurityContextHolder.getContext();
        if(!EMPTY_CONTEXT.equals(originalContext)) {
            getContexts().push(originalContext);
        }

        SecurityContextHolder.setContext(newContext);
        return message;
    }

    private void cleanup() {
        Stack<SecurityContext> stack = getContexts();

        if(stack.isEmpty()) {
            CONTEXT_STACK.remove();
            SecurityContextHolder.clearContext();
        } else {
            SecurityContext previousContext = stack.pop();
            SecurityContextHolder.setContext(previousContext);
        }
    }

    private Stack<SecurityContext> getContexts() {
        Stack<SecurityContext> stack = CONTEXT_STACK.get();
        if(stack == null) {
            stack = new Stack<SecurityContext>();
            CONTEXT_STACK.set(stack);
        }
        return stack;
    }
}
