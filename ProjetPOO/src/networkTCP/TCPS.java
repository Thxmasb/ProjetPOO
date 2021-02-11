package networkTCP;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import graphicalInterface.DiscutionWindow;
import other.User;

public class TCPS extends Thread{


	private ServerSocket sockListenTCP;
	TCPC client; ArrayList<User> Liste;
	
	public TCPS(ArrayList<User> Liste) throws UnknownHostException, IOException {
		//We create a TCP server that listens on port 3500. 
		this.sockListenTCP = new ServerSocket(3500, 5, InetAddress.getLocalHost());
		//This list includes the list of active users
		this.Liste=Liste;
	}
	
	public void run() {
		System.out.println("TCP running");
		Socket sockAccept;
		while(true) {
			try {
				//Listen to a connection to be made and accept it
				sockAccept = this.sockListenTCP.accept();
				//We create a new client that will allow us to communicate with the other user.
				TCPC clients=new TCPC(sockAccept);
				//We open a conversation window with the person who requested a connection.
				for (User u:Liste){
					if (u.getAddress().equals(sockAccept.getInetAddress())){
						new DiscutionWindow(u,clients);
					}
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}