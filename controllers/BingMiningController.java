package controllers;

import servers.BingMiningServer;
import servers.ConnectionCallback;
import views.BingMiningView;
import views.OnCloseCallback;

public class BingMiningController {

	private BingMiningView view;
	
	private BingMiningServer server;

	public BingMiningController() {
		view = new BingMiningView();
		
		server = new BingMiningServer(BingMiningServer.DEFAULT_PORT);
		server.addConnectionCallback(new ConnectionCallback() {
			@Override
			public void onConnect(BingMiningServer.ClientSocket cs) {
				view.setUserAmount(server.getNumConnections());
			}
			
			@Override
			public void onDisconnect(BingMiningServer.ClientSocket cs) {
				view.setUserAmount(server.getNumConnections());
			}
		});
		
		view.addOnCloseCallback(() -> {
			server.stopServer();
		});
	}

}