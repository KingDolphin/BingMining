package controllers;

import servers.BingMiningServer;
import servers.ConnectionCallback;
import views.BingMiningView;
import views.OnCloseCallback;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;

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
		
		try {
			Process cmdProc = Runtime.getRuntime().exec("./select.sh");
			
			BufferedReader r = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
			String table = "", line;
			while ((line = r.readLine()) != null)
				table += line + "\n";
			BufferedReader r2 = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
			while ((line = r2.readLine()) != null)
				System.err.println(line);
				
			view.setDBText(table);
			System.out.println(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}