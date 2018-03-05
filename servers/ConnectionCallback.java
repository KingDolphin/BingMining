package servers;

public interface ConnectionCallback {

	public void onConnect(BingMiningServer.ClientSocket cs);
	
	public void onDisconnect(BingMiningServer.ClientSocket cs);

}