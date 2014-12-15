package org.springframework.session.data.redis.config.annotation.web.http;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class EnableRedisKeyspaceNotificationsInitializerTests {
    static final String CONFIG_NOTIFY_KEYSPACE_EVENTS = "notify-keyspace-events";
    static final String CONFIG_NOTIFY_KEYSPACE_EVENTS_KEYSPACE = "K";

    @Mock
    RedisConnectionFactory connectionFactory;
    @Mock
    RedisConnection connection;

    EnableRedisKeyspaceNotificationsInitializer initializer;

    @Before
    public void setup() {
        when(connectionFactory.getConnection()).thenReturn(connection);

        initializer = new EnableRedisKeyspaceNotificationsInitializer(connectionFactory);
    }

    @Test
    public void afterPropertiesSetUnset() throws Exception {
        setConfigNotification("");

        initializer.afterPropertiesSet();

        verify(connection).setConfig(CONFIG_NOTIFY_KEYSPACE_EVENTS, CONFIG_NOTIFY_KEYSPACE_EVENTS_KEYSPACE);
    }

    @Test
    public void afterPropertiesSetE() throws Exception {
        setConfigNotification("E");

        initializer.afterPropertiesSet();

        verify(connection).setConfig(CONFIG_NOTIFY_KEYSPACE_EVENTS, "E"+CONFIG_NOTIFY_KEYSPACE_EVENTS_KEYSPACE);
    }

    @Test
    public void afterPropertiesSetK() throws Exception {
        setConfigNotification("K");

        initializer.afterPropertiesSet();

        verify(connection, never()).setConfig(anyString(), anyString());
    }

    @Test
    public void afterPropertiesSetEK() throws Exception {
        setConfigNotification("EK");

        initializer.afterPropertiesSet();

        verify(connection, never()).setConfig(anyString(), anyString());
    }

    @Test
    public void afterPropertiesSetAEK() throws Exception {
        setConfigNotification("AEK");

        initializer.afterPropertiesSet();

        verify(connection, never()).setConfig(anyString(), anyString());
    }

    private void setConfigNotification(String value) {
        when(connection.getConfig(CONFIG_NOTIFY_KEYSPACE_EVENTS)).thenReturn(Arrays.asList(CONFIG_NOTIFY_KEYSPACE_EVENTS, value));
    }
}