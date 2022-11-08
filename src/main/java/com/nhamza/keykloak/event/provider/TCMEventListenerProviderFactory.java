package com.nhamza.keykloak.event.provider;

import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class TCMEventListenerProviderFactory implements EventListenerProviderFactory {

	private TCMConfig cfg;

	@Override
	public EventListenerProvider create(KeycloakSession session) {
		return new TCMEventListenerProvider(cfg, session);
	}

	@Override
	public void init(Scope config) {
		cfg = TCMConfig.createFromScope(config);
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {

	}

	@Override
	public void close() {

	}

	@Override
	public String getId() {
		return "keycloak-to-stc_tcm";
	}

}
