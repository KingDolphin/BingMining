import controllers.BingMiningController;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;

public class BingMining {
	
	public static void main(String[] args) throws Exception {
		//new BingMiningController();
		
		
		Process cmdProc = Runtime.getRuntime().exec("ls", null, new File("db"));
		
		BufferedReader r = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
		String line;
		while ((line = r.readLine()) != null) {
			System.out.println(line);
		}
		
		BufferedReader r2 = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
		while ((line = r2.readLine()) != null) {
			System.err.println(line);
		}
		
		int retValue = cmdProc.exitValue();
		
	}
	
}