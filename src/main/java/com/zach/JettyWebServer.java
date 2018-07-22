package com.zach;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyWebServer {

	private final Server jettyServer = new Server();
	private int port;
	private String webappPath;

	public JettyWebServer() {

	}

	public void init() {
		ServerConnector connector = new ServerConnector(jettyServer);
		connector.setPort(this.port);
//		connector.setMaxIdleTime(30000);
		connector.setIdleTimeout(30000);

		this.jettyServer.addConnector(connector);

		WebAppContext contextHandler = new WebAppContext();
		contextHandler.setContextPath("/");
//		contextHandler.setWar("../webapp");
		contextHandler.setWar(webappPath);

		this.jettyServer.setHandler(contextHandler);
	}

	public void start() throws Exception {
		this.jettyServer.start();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public String getWebappPath() {
		return webappPath;
	}

	public void setWebappPath(String webappPath) {
		this.webappPath = webappPath;
	}
}
