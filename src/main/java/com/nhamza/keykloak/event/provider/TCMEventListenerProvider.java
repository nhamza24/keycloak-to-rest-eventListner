package com.nhamza.keykloak.event.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerTransaction;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;

public class TCMEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(TCMEventListenerProvider.class);

    private final TCMConfig cfg;
    private final CloseableHttpClient client;
    private static final int REQUEST_TIMEOUT = 30000;
    private static final int CONNECT_TIMEOUT = 30000;
    // The timeout for waiting for data
    private static final int SOCKET_TIMEOUT = 60000;

    private final EventListenerTransaction tx =
        new EventListenerTransaction(this::publishAdminEvent, this::publishEvent);

    public TCMEventListenerProvider(TCMConfig cfg, KeycloakSession session) {
        this.cfg = cfg;

        this.client = HttpClients.createDefault();
        session.getTransactionManager().enlistAfterCompletion(tx);

    }

    @Override
    public void close() {

    }

    @Override
    public void onEvent(Event event) {
        tx.addEvent(event);
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        tx.addAdminEvent(adminEvent, includeRepresentation);
    }

    private void publishEvent(Event event) {
        EventClientNotificationMqMsg msg = EventClientNotificationMqMsg.create(event);
        String messageString = TCMConfig.writeAsJson(msg, true);
        if (messageString.contains("\"resourceType\" : \"USER\"")) {
            this.publishNotification(messageString);
        }
    }

    private void publishAdminEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        EventAdminNotificationMqMsg msg = EventAdminNotificationMqMsg.create(adminEvent);
        String messageString = TCMConfig.writeAsJson(msg, true);
      if (messageString.contains("\"resourceType\" : \"USER\"")) {
            this.publishNotification(messageString);
        }
    }

    private void publishNotification(String messageString) {

        try {
            log.info("host :" + cfg.getHostUrl() + " endpoint :" + cfg.getEndpoint() + " port: " + cfg.getPort());
            HttpPost httpPost = new HttpPost("http://" + cfg.getHostUrl() + ":" + cfg.getPort() + cfg.getEndpoint());
            final HttpHost proxy = new HttpHost(cfg.getHostUrl(), cfg.getPort());
            RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
            httpPost.setConfig(requestConfig);
            StringEntity entity = new StringEntity(messageString, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
                  httpPost.setHeader("Accept", "application/json");
            client.execute(httpPost);
            log.infof("keycloak-to-TCM SUCCESS sending message: %s%n", messageString);

        } catch (Exception ex) {
            log.errorf(ex, "keycloak-to-TCM ERROR sending message: %s%n", messageString);
        }
    }

}
