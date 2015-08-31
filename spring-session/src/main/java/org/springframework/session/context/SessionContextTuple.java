package org.springframework.session.context;

import org.springframework.session.Session;

class SessionContextTuple {

	private Session session;

	private SessionContext context;

	public void setContext(SessionContext context) {
		this.context = context;
	}

	public void setSession(Session session) {
		if (session == null) {
			throw new IllegalArgumentException("session must be not null");
		}
		this.session = session;
	}

	public SessionContext getContext() {
		return context;
	}

	public Session getSession() {
		if (session == null && context != null) {
			context.setup();
		}
		return session;
	}
	
	public void clear() {
		session = null;
		context = null;
	}

}
